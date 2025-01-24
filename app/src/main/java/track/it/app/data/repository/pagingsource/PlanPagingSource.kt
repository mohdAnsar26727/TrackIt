package track.it.app.data.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import track.it.app.data.local.dao.PlanDao
import track.it.app.data.local.dao.TransactionDao
import track.it.app.data.local.entity.PlanEntity
import track.it.app.data.local.entity.TransactionEntity
import track.it.app.domain.model.Plan
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanMetrics
import track.it.app.domain.model.TransactionStatus

class PlanPagingSource(
    private val planDao: PlanDao,
    private val transactionDao: TransactionDao
) : PagingSource<Int, PlanDetails>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlanDetails> {
        return try {
            // Load the current page
            val page = params.key ?: 0
            val pageSize = params.loadSize

            // Fetch the paginated plans
            val planEntities = planDao.getPlansPaginated(
                page * pageSize,
                pageSize
            )

            // For each plan, fetch transactions and calculate derived fields
            val plans = planEntities.map { planEntity ->
                val transactions = transactionDao.getTransactionsByPlanId(planEntity.id)
                mapPlanEntityWithTransactions(
                    planEntity,
                    transactions
                )
            }

            if (plans.isEmpty() && page == 0) {
                throw NoSuchElementException("Looks like you don't have any plans!")
            } else {
                LoadResult.Page(
                    data = plans,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (planEntities.isEmpty()) null else page + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PlanDetails>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

fun mapPlanEntityWithTransactions(
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

