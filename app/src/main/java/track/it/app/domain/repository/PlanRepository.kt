package track.it.app.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import track.it.app.domain.model.Plan
import track.it.app.domain.model.PlanDetails

interface PlanRepository {
    fun getPlans(query: String): Flow<PagingData<PlanDetails>>
    suspend fun getPlan(id: Long): PlanDetails
    suspend fun updatePlan(plan: Plan)
    suspend fun addPlan(plan: Plan): Long
    suspend fun deletePlan(id: Long): Boolean
}