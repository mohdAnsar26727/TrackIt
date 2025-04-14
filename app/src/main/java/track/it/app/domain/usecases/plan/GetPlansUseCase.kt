package track.it.app.domain.usecases.plan

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanSortType
import track.it.app.domain.model.SortOrder
import track.it.app.domain.repository.PlanRepository

class GetPlansUseCase(
    private val planRepository: PlanRepository
) {
    operator fun invoke(
        query: String,
        sortBy: PlanSortType?,
        sortOrder: SortOrder?
    ): Flow<PagingData<PlanDetails>> =
        planRepository.getPlans(query, sortBy, sortOrder)
}
