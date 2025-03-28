package track.it.app.domain.validator


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
    override fun isValid(input: String) = input.length >= minLength
}

// ✅ Ensures only valid characters are used (letters, numbers, spaces)
object ValidCharactersRule : ValidationRule<String> {
    override fun isValid(input: String) = input.matches(Regex("^[a-zA-Z0-9 ]+$"))
}
