package track.it.app.domain.usecases.transaction


import track.it.app.domain.repository.TransactionRepository

class AddTransactionImagesUseCase(
    private val repo: TransactionRepository
) {
    suspend operator fun invoke(
        billImages: List<String>,
        transactionId: Long,
        planId: Long
    ): Result<Unit> {
        return runCatching {
            repo.addTransactionProof(
                billImages,
                transactionId,
                planId = planId
            )
        }
    }
}
