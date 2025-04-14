package track.it.app.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import track.it.app.domain.model.Plan
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanSortType
import track.it.app.domain.model.SortOrder

interface PlanRepository {
    fun getPlans(
        search: String?,
        sortBy: PlanSortType?,
        sortOrder: SortOrder?
    ): Flow<PagingData<PlanDetails>>
    suspend fun getPlan(id: Long): Result<PlanDetails>
    suspend fun updatePlan(plan: Plan): Result<Unit>
    suspend fun addPlan(plan: Plan): Result<Long>
    suspend fun deletePlan(id: Long): Result<Boolean>
}