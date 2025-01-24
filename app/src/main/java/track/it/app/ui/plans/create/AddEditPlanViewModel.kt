package track.it.app.ui.plans.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import track.it.app.domain.model.Plan
import track.it.app.domain.usecases.plan.CreatePlanUseCase
import track.it.app.domain.usecases.plan.GetPlanUseCase
import track.it.app.domain.usecases.plan.UpdatePlanUseCase
import javax.inject.Inject

data class PlanFormState(
    val title: String = "",
    val description: String = "",
    val budget: String = "",
)

@HiltViewModel
class AddEditPlanViewModel @Inject constructor(
    private val createPlanUseCase: CreatePlanUseCase,
    private val updatePlanUseCase: UpdatePlanUseCase,
    private val getPlanUseCase: GetPlanUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(PlanFormState())
    val formState = _formState.asStateFlow()

    private val _planResult = MutableStateFlow<Result<Unit>?>(null)
    val planResult = _planResult.asSharedFlow()


    fun setPlanIdForEdit(planId: Long) {
        viewModelScope.launch {
            val plan = getPlanUseCase(planId)?.plan ?: return@launch
            _formState.value = _formState.value.copy(
                title = plan.title,
                description = plan.description,
                budget = plan.initialBudget.toString()
            )
        }
    }

    fun addPlan() {
        viewModelScope.launch {
            val plan = Plan(
                title = _formState.value.title,
                description = _formState.value.description,
                initialBudget = _formState.value.budget.toDoubleOrNull() ?: 0.0
            )
            val result = createPlanUseCase(plan)
            _planResult.emit(result)
        }
    }

    fun updatePlan(id: Long) {
        viewModelScope.launch {
            val plan = getPlanUseCase(id)?.plan ?: return@launch
            val result = updatePlanUseCase(
                plan.copy(
                    title = _formState.value.title,
                    description = _formState.value.description,
                    initialBudget = _formState.value.budget.toDoubleOrNull() ?: 0.0
                )
            )
            _planResult.emit(result)
        }
    }

    fun onTitleValueChange(value: String) {
        _formState.value = _formState.value.copy(title = value)
    }

    fun onBudgetValueChange(value: String) {
        _formState.value = _formState.value.copy(budget = value)
    }

    fun onDescriptionValueChange(value: String) {
        _formState.value = _formState.value.copy(description = value)
    }
}