package track.it.app.domain.usecases.plan

import track.it.app.domain.repository.PlanRepository
import track.it.app.domain.repository.TransactionRepository

class DeletePlanUseCase(
    private val planRepository: PlanRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): Result<Boolean> = runCatching {
        transactionRepository.deleteTransactionsByPlanId(id)
        planRepository.deletePlan(id)
    }
}
