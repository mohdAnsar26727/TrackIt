package track.it.app.ui.plans.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import track.it.app.domain.model.BudgetUnit
import track.it.app.domain.model.PlanForm
import track.it.app.domain.model.ValidationResult
import track.it.app.domain.usecases.plan.GetPlanUseCase
import track.it.app.domain.usecases.plan.UpsertPlanUseCase
import track.it.app.domain.usecases.plan.ValidatePlanFormUseCase
import javax.inject.Inject

data class PlanFormState(
    val title: String = "",
    val description: String = "",
    val budget: String = "",
    val selectedBudgetUnit: BudgetUnit = BudgetUnit.LAKH,
    val validationResult: Map<PlanForm, ValidationResult> = emptyMap()
)

@HiltViewModel
class AddEditPlanViewModel @Inject constructor(
    private val upsertPlanUseCase: UpsertPlanUseCase,
    private val getPlanUseCase: GetPlanUseCase,
    private val validatePlanUseCase: ValidatePlanFormUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(PlanFormState())
    val formState = _formState.asStateFlow()

    private val _planResult = MutableStateFlow<Result<Any>?>(null)
    val planResult = _planResult.asSharedFlow()


    fun setPlanIdForEdit(planId: Long) {
        viewModelScope.launch {
            val plan = getPlanUseCase(planId).getOrNull()?.plan ?: return@launch
            _formState.value = _formState.value.copy(
                title = plan.title,
                description = plan.description,
                budget = plan.initialBudget.toString(),
                selectedBudgetUnit = BudgetUnit.CUSTOM
            )
        }
    }

    private fun isPlanDataIsValid(): Boolean {
        val validationResult = validatePlanUseCase(
            _formState.value.title,
            _formState.value.budget,
            _formState.value.description,
        )

        _formState.value = formState.value.copy(validationResult = validationResult)

        return validationResult.values.none { it is ValidationResult.Error }
    }

    fun addPlan(id: Long? = null) {

        if (!isPlanDataIsValid()) return

        viewModelScope.launch {

            val data = formState.value

            val result = upsertPlanUseCase(
                id = id,
                title = data.title,
                description = data.description,
                initialBudget = data.budget,
                budgetUnit = data.selectedBudgetUnit
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

    fun onBudgetUnitChange(newUnit: BudgetUnit) {
        _formState.value = _formState.value.copy(selectedBudgetUnit = newUnit)
    }
}