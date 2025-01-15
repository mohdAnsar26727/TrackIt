package track.it.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import track.it.app.data.local.dao.PlanDao
import track.it.app.data.local.dao.TransactionDao
import track.it.app.data.local.entity.PlanEntity
import track.it.app.data.repository.pagingsource.mapPlanEntityWithTransactions
import track.it.app.domain.model.Plan
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.repository.PlanRepository
import track.it.app.util.CoroutineExtended.doIoOperation
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val planDao: PlanDao,
    private val transactionDao: TransactionDao
) : PlanRepository {
    override fun getPlans(query: String): Flow<PagingData<PlanDetails>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // Define the page size
                enablePlaceholders = false
            ),
            pagingSourceFactory = { planDao.getAllPlansPaged(query) }
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

    override suspend fun getPlan(id: Long): PlanDetails {
        return doIoOperation {
            val plan = planDao.getPlanById(id) ?: throw Exception("No data found!")
            val transactions = transactionDao.getTransactionsByPlanId(id)
            mapPlanEntityWithTransactions(
                plan,
                transactions
            )
        }
    }

    override suspend fun updatePlan(plan: Plan) {
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

    override suspend fun addPlan(plan: Plan): Long {
        return doIoOperation {
            planDao.insertPlan(
                PlanEntity(
                    id = plan.id,
                    title = plan.title,
                    description = plan.description,
                    initialBudget = plan.initialBudget,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun deletePlan(id: Long) {
        doIoOperation {
            val plan = planDao.getPlanById(id) ?: return@doIoOperation
            planDao.deletePlan(plan)
        }
    }
}