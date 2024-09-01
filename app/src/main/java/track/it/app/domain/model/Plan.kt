package track.it.app.domain.model

import track.it.app.ui.navigation.MainRoute
import kotlin.random.Random

data class Plan(
    val id: Int,
    val title: String,
    val description: String,
    val initialBudget: Double,
    val totalSpent: Double,
    val remainingAmount: Double,
    val progress: Float, // Progress as a percentage (0.0f to 1.0f)
    val totalTransactions: Int,
    val predictedTransactions: Int,
    val paidTransactions: Int,
)

object MockPlans {
    val description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum."

    fun randomPlans(count: Int): MutableList<Plan> {
        val list = mutableListOf<Plan>()
        for (i in 0..count) {
            val plan =
            Plan(
                title = "Home Renovation work",
                initialBudget = 400000.0,
                totalSpent = 50.0,
                remainingAmount = 350000.0,
                progress = 0.2f,
                id = Random.nextInt(),
                totalTransactions = 7,
                predictedTransactions = 2,
                paidTransactions = 5,
                description = description,
            )
            list.add(plan)
        }
        return list
    }

    val plans = listOf(
        Plan(
            title = "Home Renovation work",
            initialBudget = 400000.0,
            totalSpent = 50.0,
            remainingAmount = 350000.0,
            progress = 0.2f,
            id = 1604,
            totalTransactions = 7,
            predictedTransactions = 2,
            paidTransactions = 5,
            description = description,
        ),
        Plan(
            title = "Trip to kodaikanal",
            initialBudget = 5000.0,
            totalSpent = 1200.0,
            remainingAmount = 3800.0,
            progress = 0.16f,
            id = 7732,
            totalTransactions = 5,
            predictedTransactions = 0, paidTransactions = 5, description = description
        )
    )
}