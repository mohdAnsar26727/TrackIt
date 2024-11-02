package track.it.app.domain.usecases.transaction

import track.it.app.domain.model.TransactionWithImages
import track.it.app.domain.repository.TransactionRepository

class GetTransactionUseCase(
    private val repo: TransactionRepository
) {
    suspend operator fun invoke(id: Long): TransactionWithImages? = repo.getTransaction(id)
}
