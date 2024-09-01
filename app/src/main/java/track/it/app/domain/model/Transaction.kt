package track.it.app.domain.model

data class Transaction(
    val description: String,
    val amount: Double,
    val date: String
)