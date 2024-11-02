package track.it.app.domain.usecases.plan

import track.it.app.domain.repository.PlanRepository

class DeletePlanUseCase(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(id: Long) = planRepository.deletePlan(id)
}
