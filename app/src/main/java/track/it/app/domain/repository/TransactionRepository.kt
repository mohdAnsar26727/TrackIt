package track.it.app.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import track.it.app.domain.model.Transaction
import track.it.app.domain.model.TransactionStatus
import track.it.app.domain.model.TransactionWithImages

interface TransactionRepository {
    fun getTransactions(planId: Long): Flow<PagingData<TransactionWithImages>>
    suspend fun getTransaction(id: Long): TransactionWithImages?
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun addTransaction(
        planId: Long,
        to: String,
        note: String,
        amount: Double,
        transactionStatus: TransactionStatus
    ): Long

    suspend fun deleteTransaction(id: Long)
    suspend fun addTransactionProof(
        copiedImages: List<String>,
        transactionId: Long
    )

    suspend fun deleteAllTransactionProof(transactionId: Long)
}