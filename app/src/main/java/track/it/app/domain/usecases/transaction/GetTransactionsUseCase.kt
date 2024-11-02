package track.it.app.domain.usecases.transaction

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import track.it.app.domain.model.TransactionWithImages
import track.it.app.domain.repository.TransactionRepository

class GetTransactionsUseCase(
    private val repo: TransactionRepository
) {
    operator fun invoke(planId: Long): Flow<PagingData<TransactionWithImages>> =
        repo.getTransactions(planId)
}
