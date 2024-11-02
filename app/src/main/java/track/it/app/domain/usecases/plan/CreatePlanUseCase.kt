package track.it.app.domain.usecases.plan

import track.it.app.domain.model.Plan
import track.it.app.domain.repository.PlanRepository

class CreatePlanUseCase(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(plan: Plan): Result<Unit> = runCatching {
        planRepository.addPlan(plan)
    }
}