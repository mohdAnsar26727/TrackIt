package track.it.app.domain.validator

import android.content.Context
import track.it.app.R

class PlanNameValidator(context: Context) : BaseValidator(
    context,
    mapOf(
        NotBlankRule to R.string.error_empty_plan_name,
        NoDigitsOnlyRule to R.string.error_only_numbers_plan_name,
        MinLengthRule(5) to R.string.error_short_plan_name,
        ValidCharactersRule to R.string.error_invalid_chars_plan_name
    )
)

class BudgetValidator(context: Context) : BaseValidator(
    context,
    mapOf(
        NotBlankRule to R.string.error_empty_budget,
        NumericOnlyRule to R.string.error_invalid_budget,
        PositiveNumberRule to R.string.error_negative_budget
    )
)

class PlanNoteValidator(context: Context) : BaseValidator(
    context,
    mapOf(
        NotBlankRule to R.string.error_empty_description,
        MinLengthRule(10) to R.string.error_short_description
    )
)

