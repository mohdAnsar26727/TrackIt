package track.it.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import track.it.app.data.local.dao.ImageDao
import track.it.app.data.local.dao.TransactionDao
import track.it.app.data.local.entity.ImageEntity
import track.it.app.data.local.entity.TransactionEntity
import track.it.app.data.mappers.TransactionWithImagesMapper
import track.it.app.domain.model.Transaction
import track.it.app.domain.model.TransactionStatus
import track.it.app.domain.model.TransactionWithImages
import track.it.app.domain.repository.FileRepository
import track.it.app.domain.repository.TransactionRepository
import track.it.app.util.CoroutineExtended.doIoOperation
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val imageDao: ImageDao,
    private val mapper: TransactionWithImagesMapper,
    private val fileHandler: FileRepository
) : TransactionRepository {
    override fun getTransactions(planId: Long): Flow<PagingData<TransactionWithImages>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // Define the page size
                enablePlaceholders = false
            ),
            pagingSourceFactory = { transactionDao.getTransactionsWithImagesByPlanIdPaged(planId) }
        ).flow.map { pagingData ->
            pagingData.map { transactionWithImages ->
                mapper.toDomain(transactionWithImages)
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTransaction(id: Long): TransactionWithImages? {
        return doIoOperation {
            transactionDao.getTransactionById(id)?.run { mapper.toDomain(this) }
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        doIoOperation {
            transactionDao.updateTransaction(
                TransactionEntity(
                    id = transaction.id,
                    planId = transaction.planId,
                    to = transaction.to,
                    note = transaction.note,
                    amount = transaction.amount,
                    status = transaction.status.name,
                    createdAt = transaction.createdAt,
                    updatedAt = transaction.updatedAt,
                )
            )
        }
    }

    override suspend fun addTransaction(
        planId: Long,
        to: String,
        note: String,
        amount: Double,
        transactionStatus: TransactionStatus
    ): Long {
        return doIoOperation {
            val time = System.currentTimeMillis()
            transactionDao.insertTransaction(
                TransactionEntity(
                    planId = planId,
                    to = to,
                    note = note,
                    amount = amount,
                    status = transactionStatus.name,
                    createdAt = time,
                    updatedAt = time
                )
            )
        }
    }

    override suspend fun deleteTransaction(id: Long) {
        doIoOperation {
            val transactionWithImages =
                transactionDao.getTransactionById(id) ?: return@doIoOperation
            transactionDao.deleteTransaction(transactionWithImages.transaction)
        }
    }

    override suspend fun addTransactionProof(
        copiedImages: List<String>,
        transactionId: Long,
        planId: Long
    ) {
        doIoOperation {
            val imageEntities = copiedImages.map { imageUrl ->
                ImageEntity(
                    imageUrl = imageUrl,
                    planId = planId,
                    transactionId = transactionId
                )
            }
            imageDao.insertImages(imageEntities)
        }
    }

    override suspend fun deleteAllTransactionProof(transactionId: Long) {
        doIoOperation {
            val proofs = imageDao.getImagesForTransaction(transactionId = transactionId)
            imageDao.deleteImage(proofs)
        }
    }

    override suspend fun deleteTransactionsByPlanId(planId: Long) {
        doIoOperation {
            val images = imageDao.getTransactionProofOfPlan(planId)
            imageDao.deleteImagesByPlanId(planId)
            transactionDao.deleteTransactionsByPlanId(planId)

            fileHandler.deleteImages(images.map { it.imageUrl })
        }
    }
}