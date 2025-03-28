package track.it.app.domain.usecases.plan

import track.it.app.domain.model.Plan
import track.it.app.domain.repository.PlanRepository

class UpdatePlanUseCase(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(plan: Plan) = planRepository.updatePlan(plan)
}
