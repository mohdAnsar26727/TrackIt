package track.it.app.domain.model

data class PlanDetails(
    val plan: Plan,
    val progress: PlanMetrics
)

data class Plan(
    val id: Long = 0,
    val title: String,
    val description: String,
    val initialBudget: Double,
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

data class PlanMetrics(
    val totalSpent: Double,
    val remainingAmount: Double,
    val progress: Float, // Progress as a percentage (0.0f to 1.0f)
    val totalTransactions: Int,
    val estimatedTransactions: Int,
    val paidTransactions: Int
) {
    val paidTransactionProgress: Float
        get() = if (totalTransactions == 0) 0f else paidTransactions.toFloat() / totalTransactions

    val estimatedTransactionProgress: Float
        get() = if (totalTransactions == 0) 0f else estimatedTransactions.toFloat() / totalTransactions


}