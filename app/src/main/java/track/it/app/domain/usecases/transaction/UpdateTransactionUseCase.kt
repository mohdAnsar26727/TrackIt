package track.it.app.domain.usecases.transaction

import track.it.app.domain.model.Transaction
import track.it.app.domain.repository.TransactionRepository

class UpdateTransactionUseCase(
    private val repo: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        return runCatching { repo.updateTransaction(transaction) }
    }
}
