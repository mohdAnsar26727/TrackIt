package track.it.app.domain.validator

import android.content.Context
import track.it.app.domain.model.ValidationResult

abstract class BaseValidator<T>(
    private val context: Context,
    rules: Map<ValidationRule<T>, Int> = mutableMapOf()
) {
    private val rules: MutableMap<ValidationRule<T>, Int> = rules.toMutableMap()

    fun validate(input: T): ValidationResult {
        val errors = rules.mapNotNull { (rule, errorMessageRes) ->
            if (!rule.isValid(input)) context.getString(errorMessageRes) else null
        }

        return if (errors.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(errors.joinToString("\n"))
        }
    }

    fun setRules(newRules: Map<ValidationRule<T>, Int>) {
        rules.clear()
        rules.putAll(newRules)
    }
}

interface ValidationRule<T> {
    fun isValid(input: T): Boolean
}