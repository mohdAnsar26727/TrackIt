package track.it.app.domain.usecases.plan

import track.it.app.domain.model.PlanForm
import track.it.app.domain.model.ValidationResult
import track.it.app.domain.validator.BudgetValidator
import track.it.app.domain.validator.PlanNameValidator
import track.it.app.domain.validator.PlanNoteValidator

class ValidatePlanFormUseCase(
    private val planNameValidator: PlanNameValidator,
    private val budgetValidator: BudgetValidator,
    private val planNoteValidator: PlanNoteValidator
) {
    operator fun invoke(
        planName: String,
        budget: String,
        description: String
    ): Map<PlanForm, ValidationResult> {
        return mapOf(
            PlanForm.Name to planNameValidator.validate(planName),
            PlanForm.Budget to budgetValidator.validate(budget),
            PlanForm.Note to planNoteValidator.validate(description)
        )
    }
}
