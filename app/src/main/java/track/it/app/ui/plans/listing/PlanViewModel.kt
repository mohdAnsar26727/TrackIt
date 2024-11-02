package track.it.app.ui.plans.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import track.it.app.domain.usecases.plan.DeletePlanUseCase
import track.it.app.domain.usecases.plan.GetPlansUseCase
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val deletePlanUseCase: DeletePlanUseCase,
    private val getPlansUseCase: GetPlansUseCase
) : ViewModel() {
    val plans = getPlansUseCase().cachedIn(viewModelScope)

}