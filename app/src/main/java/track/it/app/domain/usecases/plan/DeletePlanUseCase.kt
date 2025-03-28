package track.it.app.domain.usecases.plan

import androidx.room.withTransaction
import track.it.app.data.local.database.AppDatabase
import track.it.app.domain.repository.PlanRepository
import track.it.app.domain.repository.TransactionRepository

class DeletePlanUseCase(
    private val database: AppDatabase,
    private val planRepository: PlanRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Long) = runCatching {
        database.withTransaction {
            transactionRepository.deleteTransactionsByPlanId(id).getOrThrow()
            planRepository.deletePlan(id).getOrThrow()
        }
    }
}
