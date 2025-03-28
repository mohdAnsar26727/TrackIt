package track.it.app.domain.model

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val errorMessage: String) : ValidationResult()
}

