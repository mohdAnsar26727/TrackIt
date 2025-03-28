package track.it.app.domain.model

enum class BudgetUnit(val displayName: String) {
    THOUSAND("Thousand"),
    LAKH("Lakh"),
    CRORE("Crore"),
    CUSTOM("Custom")
}