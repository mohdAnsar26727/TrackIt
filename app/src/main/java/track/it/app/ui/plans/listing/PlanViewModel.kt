package track.it.app.ui.plans.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.usecases.plan.DeletePlanUseCase
import track.it.app.domain.usecases.plan.GetPlansUseCase
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val deletePlanUseCase: DeletePlanUseCase,
    private val getPlansUseCase: GetPlansUseCase
) : ViewModel() {
    var plansFlow: Flow<PagingData<PlanDetails>> = flowOf(PagingData.empty())

    init {
        getPlans()
    }

    fun getPlans(query: String = "") {
        plansFlow = getPlansUseCase(query).cachedIn(viewModelScope)
    }
}