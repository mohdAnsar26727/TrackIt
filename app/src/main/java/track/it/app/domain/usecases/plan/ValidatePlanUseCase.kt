package track.it.app.domain.usecases.plan

import track.it.app.domain.model.FormField
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
    ): Map<FormField, ValidationResult> {
        return mapOf(
            FormField.PlanName to planNameValidator.validate(planName),
            FormField.Budget to budgetValidator.validate(budget),
            FormField.Note to planNoteValidator.validate(description)
        )
    }
}
