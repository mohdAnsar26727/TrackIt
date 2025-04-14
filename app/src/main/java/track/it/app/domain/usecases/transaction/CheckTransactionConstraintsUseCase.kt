package track.it.app.domain.usecases.transaction

import track.it.app.domain.model.ValidationResult
import track.it.app.domain.validator.TransactionAmountConstraintsValidator
import javax.inject.Inject

class CheckTransactionConstraintsUseCase @Inject constructor(
    val amountValidator: TransactionAmountConstraintsValidator
) {

    operator fun invoke(
        amount: Double,
        budget: Double,
        date: Long,
        maxDate: Long
    ): ValidationResult {

        val amountResult = amountValidator.validate(amount, budget)

        return amountResult
    }
}
