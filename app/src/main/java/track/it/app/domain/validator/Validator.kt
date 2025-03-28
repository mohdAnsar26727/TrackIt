package track.it.app.domain.validator

import android.content.Context
import track.it.app.domain.model.ValidationResult

abstract class BaseValidator(
    private val context: Context,
    private val rules: Map<ValidationRule<String>, Int>
) {
    fun validate(input: String): ValidationResult {
        val errors = rules.mapNotNull { (rule, errorMessageRes) ->
            if (!rule.isValid(input)) context.getString(errorMessageRes) else null
        }

        return if (errors.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(errors.joinToString("\n"))
        }
    }
}

interface ValidationRule<T> {
    fun isValid(input: T): Boolean
}