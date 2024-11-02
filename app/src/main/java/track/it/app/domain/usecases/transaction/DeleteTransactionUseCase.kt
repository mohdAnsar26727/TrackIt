package track.it.app.domain.usecases.transaction

import track.it.app.domain.repository.TransactionRepository

class DeleteTransactionUseCase(
    private val repo: TransactionRepository
) {
    suspend operator fun invoke(id: Long) =
        runCatching {
            repo.deleteTransaction(id)
        }
}
