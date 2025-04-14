package track.it.app.domain.usecases.transaction

import android.net.Uri
import track.it.app.domain.model.TransactionForm
import track.it.app.domain.model.ValidationResult
import track.it.app.domain.validator.TransactionAmountValidator
import track.it.app.domain.validator.TransactionImagesValidator
import track.it.app.domain.validator.TransactionNoteValidator
import track.it.app.domain.validator.TransactionReceiverNameValidator

class ValidateTransactionFormUseCase(
    private val transactionReceiverNameValidator: TransactionReceiverNameValidator,
    private val transactionAmountValidator: TransactionAmountValidator,
    private val transactionNoteValidator: TransactionNoteValidator,
    private val transactionImagesValidator: TransactionImagesValidator
) {
    operator fun invoke(
        to: String,
        amount: String,
        note: String,
        images: Set<Uri>,
    ): Map<TransactionForm, ValidationResult> {
        return mapOf(
            TransactionForm.To to transactionReceiverNameValidator.validate(to),
            TransactionForm.Amount to transactionAmountValidator.validate(amount),
            TransactionForm.Note to transactionNoteValidator.validate(note),
            TransactionForm.Image to transactionImagesValidator.validate(images),
        )
    }
}
