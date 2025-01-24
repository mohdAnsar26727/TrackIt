package track.it.app.ui.plans.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.usecases.plan.DeletePlanUseCase
import track.it.app.domain.usecases.plan.GetPlanUseCase
import track.it.app.domain.usecases.transaction.GetTransactionsUseCase

class PlanDetailsViewModel @AssistedInject constructor(
    private val getPlanUseCase: GetPlanUseCase,
    private val deletePlanUseCase: DeletePlanUseCase,
    getTransactionsUseCase: GetTransactionsUseCase,
    @Assisted private val planId: Long
) : ViewModel() {
    @AssistedFactory
    interface PlanDetailsViewModelFactory {
        fun create(planId: Long): PlanDetailsViewModel
    }

    val transactions = getTransactionsUseCase(planId).cachedIn(viewModelScope)

    private val _planById = MutableStateFlow<PlanDetails?>(null)
    val planById: StateFlow<PlanDetails?> = _planById
        .onStart {
            getPlanById(planId)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    private fun getPlanById(id: Long) {
        viewModelScope.launch {
            _planById.value = getPlanUseCase(id)
        }
    }

    fun deletePlan(id: Long) {
        viewModelScope.launch {
            deletePlanUseCase(id)
        }
    }
}