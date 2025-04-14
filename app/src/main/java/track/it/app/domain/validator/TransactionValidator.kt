package track.it.app.domain.validator

import android.content.Context
import android.net.Uri
import track.it.app.R
import track.it.app.domain.model.ValidationResult
import java.io.File

class TransactionReceiverNameValidator(context: Context) : BaseValidator<String>(
    context,
    mapOf(
        NotBlankRule to R.string.error_empty_transaction_receiver_name,
        NoDigitsOnlyRule to R.string.error_only_numbers_in_text,
        MinLengthRule(5) to R.string.error_short_plan_in_text,
        ValidCharactersRule to R.string.error_invalid_chars_in_text
    )
)

class TransactionAmountConstraintsValidator(val context: Context) {

    private val validator = object : BaseValidator<Double>(context, mutableMapOf()) {}

    fun validate(amount: Double, maxAmount: Double): ValidationResult {
        validator.setRules(
            mapOf(
                LessThanTargetRule(maxAmount) to R.string.error_exceed_budget
            )
        )

        return validator.validate(amount)
    }
}

class TransactionAmountValidator(val context: Context) {

    fun validate(input: String): ValidationResult {

        val transactionValidator = object : BaseValidator<String>(
            context = context,
            mapOf(
                NotBlankRule to R.string.error_empty_amount,
                NumericOnlyRule to R.string.error_invalid_amount,
                PositiveNumberRule to R.string.error_negative_amount,
            )
        ) {}

        return transactionValidator.validate(input)
    }
}

class TransactionNoteValidator(context: Context) : BaseValidator<String>(
    context,
    mapOf(
        MinLengthRule(10) to R.string.error_short_description
    )
)

class TransactionImagesValidator(context: Context) {
    private val fileValidator = object : BaseValidator<File>(
        context,
        mapOf(
            FileExistRule to R.string.file_not_found,
        )
    ) {}

    fun validate(image: Set<Uri>): ValidationResult {
        val result = image.map { uri ->
            val file = File(uri.toString())
            fileValidator.validate(file)
        }

        return result.firstOrNull { it is ValidationResult.Error } ?: ValidationResult.Success
    }
}

