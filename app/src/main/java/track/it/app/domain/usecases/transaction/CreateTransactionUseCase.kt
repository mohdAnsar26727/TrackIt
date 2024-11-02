package track.it.app.domain.usecases.transaction

import track.it.app.domain.model.TransactionStatus
import track.it.app.domain.repository.TransactionRepository

class CreateTransactionUseCase(
    private val repo: TransactionRepository,
    private val billImageFilesUseCase: CopyBillImageFilesUseCase,
    private val addTransactionImagesUseCase: AddTransactionImagesUseCase,
    private val deleteBillImageFilesUseCase: DeleteBillImageFilesUseCase,
) {
    suspend operator fun invoke(
        planId: Long,
        to: String,
        note: String,
        amount: String,
        transactionStatus: TransactionStatus,
        images: List<String>? = null
    ) = runCatching {
        val transactionAmount = amount.toDoubleOrNull() ?: 0.0
        val transactionId = repo.addTransaction(
            planId,
            to,
            note,
            transactionAmount,
            transactionStatus
        )
        if (transactionId <= 0) throw Exception("Failed to create transaction!")

        if (images.isNullOrEmpty()) return@runCatching

        val copyImagesResult = billImageFilesUseCase(
            images,
            to
        )

        copyImagesResult.onFailure { repo.deleteTransaction(transactionId) }

        copyImagesResult.onSuccess { copiedImages ->
            val transactionImageResult = addTransactionImagesUseCase(
                copiedImages,
                transactionId
            )

            transactionImageResult.onFailure {
                deleteBillImageFilesUseCase(copiedImages)
            }
        }
    }
}