package track.it.app.domain.validator

import android.content.Context
import track.it.app.R

class PlanNameValidator(context: Context) : BaseValidator<String>(
    context,
    mapOf(
        NotBlankRule to R.string.error_empty_plan_name,
        NoDigitsOnlyRule to R.string.error_only_numbers_in_text,
        MinLengthRule(5) to R.string.error_short_plan_in_text,
        ValidCharactersRule to R.string.error_invalid_chars_in_text
    )
)

class BudgetValidator(context: Context) : BaseValidator<String>(
    context,
    mapOf(
        NotBlankRule to R.string.error_empty_amount,
        NumericOnlyRule to R.string.error_invalid_amount,
        PositiveNumberRule to R.string.error_negative_amount
    )
)

class PlanNoteValidator(context: Context) : BaseValidator<String>(
    context,
    mapOf(
        NotBlankRule to R.string.error_empty_description,
        MinLengthRule(10) to R.string.error_short_description
    )
)

