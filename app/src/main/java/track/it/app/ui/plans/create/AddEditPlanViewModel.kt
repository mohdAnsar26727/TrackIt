package track.it.app.ui.plans.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import track.it.app.domain.model.Plan
import track.it.app.domain.model.TransactionStatus
import track.it.app.domain.usecases.plan.CreatePlanUseCase
import track.it.app.domain.usecases.plan.GetPlanUseCase
import track.it.app.domain.usecases.plan.UpdatePlanUseCase
import javax.inject.Inject

@HiltViewModel
class AddEditPlanViewModel @Inject constructor(
    private val createPlanUseCase: CreatePlanUseCase,
    private val updatePlanUseCase: UpdatePlanUseCase,
    private val getPlanUseCase: GetPlanUseCase
) : ViewModel() {

    var title by mutableStateOf("")
        private set
    var budget by mutableStateOf("")
        private set
    var descriptions by mutableStateOf("")
        private set

    private val _editPlan = MutableStateFlow(0L)
    val editPlan = _editPlan.onStart {
        getPlanUseCase(_editPlan.value)?.plan?.let {
            title = it.title
            budget = it.initialBudget.toString()
            descriptions = it.description
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    var planResult by mutableStateOf<Result<Unit>?>(null)
        private set

    fun setPlanIdForEdit(planId: Long) {
        _editPlan.value = planId
    }

    fun addPlans(plan: Plan) {
        viewModelScope.launch {
            planResult = createPlanUseCase(plan)
        }
    }

    fun updatePlans(plan: Plan) {
        viewModelScope.launch {
            planResult = updatePlanUseCase(plan)
        }
    }

    fun onTitleValueChange(value: String) {
        title = value
    }

    fun onBudgetValueChange(value: String) {
        budget = value
    }

    fun onDescriptionValueChange(value: String) {
        descriptions = value
    }
}