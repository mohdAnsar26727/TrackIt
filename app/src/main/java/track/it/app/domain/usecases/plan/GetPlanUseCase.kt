package track.it.app.domain.usecases.plan

import track.it.app.domain.model.PlanDetails
import track.it.app.domain.repository.PlanRepository

class GetPlanUseCase(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(id: Long): Result<PlanDetails> = planRepository.getPlan(id)
}
