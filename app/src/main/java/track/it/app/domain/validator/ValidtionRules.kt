package track.it.app.domain.validator

import java.io.File


// ✅ Ensures the input is not blank
object NotBlankRule : ValidationRule<String> {
    override fun isValid(input: String) = input.isNotBlank()
}

// ✅ Ensures the input is not only digits
object NoDigitsOnlyRule : ValidationRule<String> {
    override fun isValid(input: String) = input.any { !it.isDigit() }
}

// ✅ Ensures the input contains only numbers (for Budget)
object NumericOnlyRule : ValidationRule<String> {
    override fun isValid(input: String) = input.toDoubleOrNull() != null
}

// ✅ Ensures the input is a positive number (for Budget)
object PositiveNumberRule : ValidationRule<String> {
    override fun isValid(input: String) = input.toDoubleOrNull()?.let { it > 0 } ?: false
}

// ✅ Ensures the input meets a minimum length
class MinLengthRule(private val minLength: Int) : ValidationRule<String> {
    override fun isValid(input: String) = input.isBlank() || input.length >= minLength
}

// ✅ Ensures only valid characters are used (letters, numbers, spaces)
object ValidCharactersRule : ValidationRule<String> {
    override fun isValid(input: String) = input.matches(Regex("^[a-zA-Z0-9 ]+$"))
}

// ✅ Ensures the input is less or equal than the target
class LessThanTargetRule<T>(private val target: T) : ValidationRule<T> {
    override fun isValid(input: T) = when {
        target is Int && input is Int -> input < target
        target is Double && input is Double -> input < target
        target is Float && input is Float -> input < target
        target is Long && input is Long -> input < target
        else -> false
    }
}

object FileExistRule : ValidationRule<File> {
    override fun isValid(input: File): Boolean = input.exists()
}

