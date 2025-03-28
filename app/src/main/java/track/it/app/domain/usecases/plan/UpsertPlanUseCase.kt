package track.it.app.domain.usecases.plan

import track.it.app.domain.model.BudgetUnit
import track.it.app.domain.model.BudgetUnit.CRORE
import track.it.app.domain.model.BudgetUnit.CUSTOM
import track.it.app.domain.model.BudgetUnit.LAKH
import track.it.app.domain.model.BudgetUnit.THOUSAND
import track.it.app.domain.model.Plan
import track.it.app.domain.repository.PlanRepository

class UpsertPlanUseCase(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(
        id: Long? = null,
        title: String,
        description: String,
        initialBudget: String,
        budgetUnit: BudgetUnit
    ): Result<Long> = runCatching {
        val budget = initialBudget.toDouble()

        val converted = budget * (when (budgetUnit) {
            THOUSAND -> 1000
            LAKH -> 100000
            CRORE -> 10000000
            CUSTOM -> 1
        })

        val plan = Plan(
            id = id ?: -1,
            title = title,
            description = description,
            initialBudget = converted
        )
        planRepository.addPlan(plan).getOrThrow()
    }
}