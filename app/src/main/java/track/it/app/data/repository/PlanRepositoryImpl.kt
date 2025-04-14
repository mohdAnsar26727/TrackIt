package track.it.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import track.it.app.data.local.dao.PlanDao
import track.it.app.data.local.dao.TransactionDao
import track.it.app.data.local.entity.PlanEntity
import track.it.app.data.local.entity.TransactionEntity
import track.it.app.domain.model.Plan
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanMetrics
import track.it.app.domain.model.PlanSortType
import track.it.app.domain.model.SortOrder
import track.it.app.domain.model.TransactionStatus
import track.it.app.domain.repository.PlanRepository
import track.it.app.util.CoroutineExtended.doIoOperation
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val planDao: PlanDao,
    private val transactionDao: TransactionDao
) : PlanRepository {
    override fun getPlans(
        search: String?,
        sortBy: PlanSortType?,
        sortOrder: SortOrder?
    ): Flow<PagingData<PlanDetails>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // Define the page size
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                val query = buildPagedPlansQuery(
                    search, sortBy, sortOrder
                )
                planDao.getAllPlansPaged(query)
            }
        ).flow.map { pagingData ->
            pagingData.map { plan ->
                val transactions = transactionDao.getTransactionsByPlanId(plan.id)
                mapPlanEntityWithTransactions(
                    plan,
                    transactions
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getPlan(id: Long): Result<PlanDetails> = runCatching {
        doIoOperation {
            val plan = planDao.getPlanById(id) ?: throw Exception("No data found!")
            val transactions = transactionDao.getTransactionsByPlanId(id)
            mapPlanEntityWithTransactions(
                plan,
                transactions
            )
        }
    }

    override suspend fun updatePlan(plan: Plan): Result<Unit> = runCatching {
        doIoOperation {
            planDao.updatePlan(
                PlanEntity(
                    id = plan.id,
                    title = plan.title,
                    description = plan.description,
                    initialBudget = plan.initialBudget,
                    createdAt = plan.createdAt,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun addPlan(plan: Plan): Result<Long> = runCatching {
        doIoOperation {

            val data = getPlan(plan.id).getOrNull()?.plan

            planDao.upsertPlan(
                PlanEntity(
                    id = data?.id ?: 0,
                    title = plan.title,
                    description = plan.description,
                    initialBudget = plan.initialBudget,
                    createdAt = data?.createdAt ?: System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun deletePlan(id: Long): Result<Boolean> = runCatching {
        doIoOperation {
            val result = planDao.deletePlan(id)
            return@doIoOperation result > 0
        }
    }

    private fun buildPagedPlansQuery(
        search: String?,
        sortType: PlanSortType?, // Domain model
        sortOrder: SortOrder?
    ): SupportSQLiteQuery {

        val sql = buildString {
            append("SELECT * FROM plans ")

            if (sortType != null && sortOrder != null) {
                val validSortColumns =
                    setOf("id", "title", "createdAt", "updatedAt", "initialBudget")
                val safeSortBy =
                    if (sortType.column in validSortColumns) sortType.column else "createdAt"
                val safeSortOrder = sortOrder.name

                if (!search.isNullOrBlank()) {
                    append("WHERE title LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%' ")
                }

                append("ORDER BY $safeSortBy $safeSortOrder")
            }
        }


        val args: Array<Any?> =
            if (!search.isNullOrBlank()) arrayOf(search, search) else emptyArray()

        return SimpleSQLiteQuery(sql, args)
    }

    private fun mapPlanEntityWithTransactions(
        planEntity: PlanEntity,
        transactions: List<TransactionEntity>
    ): PlanDetails {
        // Calculate additional details based on transactions
        val totalSpent = transactions.filter {
            it.status.equals(
                TransactionStatus.PAID.name,
                ignoreCase = true
            )
        }.sumOf { it.amount }

        val predictedTransactions = transactions.count {
            it.status.equals(
                TransactionStatus.ESTIMATED.name,
                ignoreCase = true
            )
        }

        val paidTransactions = transactions.count {
            it.status.equals(
                TransactionStatus.PAID.name,
                ignoreCase = true
            )
        }

        val remainingAmount = planEntity.initialBudget - totalSpent
        val progress =
            if (planEntity.initialBudget > 0) (totalSpent / planEntity.initialBudget).toFloat() else 0f
        val totalTransactions = transactions.size

        val plan = Plan(
            id = planEntity.id,
            title = planEntity.title,
            description = planEntity.description,
            initialBudget = planEntity.initialBudget,
            createdAt = planEntity.createdAt,
            updatedAt = planEntity.updatedAt
        )

        val metrics = PlanMetrics(
            totalSpent = totalSpent,
            remainingAmount = remainingAmount,
            progress = progress,
            totalTransactions = totalTransactions,
            estimatedTransactions = predictedTransactions,
            paidTransactions = paidTransactions
        )

        return PlanDetails(
            plan,
            metrics
        )
    }

}