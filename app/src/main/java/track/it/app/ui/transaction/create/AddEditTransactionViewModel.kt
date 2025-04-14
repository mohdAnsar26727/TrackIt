package track.it.app.ui.transaction.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import track.it.app.domain.model.BudgetUnit
import track.it.app.domain.model.TransactionForm
import track.it.app.domain.model.TransactionStatus
import track.it.app.domain.model.ValidationResult
import track.it.app.domain.usecases.plan.GetPlanUseCase
import track.it.app.domain.usecases.transaction.CheckTransactionConstraintsUseCase
import track.it.app.domain.usecases.transaction.CreateTransactionUseCase
import track.it.app.domain.usecases.transaction.GetTransactionUseCase
import track.it.app.domain.usecases.transaction.ValidateTransactionFormUseCase

data class TransactionFormState(
    val to: String = "",
    val note: String = "",
    val amount: String = "",
    val date: Long = System.currentTimeMillis(),
    val transactionStatus: TransactionStatus = TransactionStatus.PAID,
    val images: Set<Uri> = emptySet(),
    val selectedBudgetUnit: BudgetUnit = BudgetUnit.LAKH,
    val validationResult: Map<TransactionForm, ValidationResult> = emptyMap()
)

class AddEditTransactionViewModel @AssistedInject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
    private val validateTransactionFormUseCase: ValidateTransactionFormUseCase,
    private val getPlanUseCase: GetPlanUseCase,
    private val checkTransactionConstraintsUseCase: CheckTransactionConstraintsUseCase,
    @Assisted val planId: Long
) : ViewModel() {

    @AssistedFactory
    interface AddEditTransactionViewModelFactory {
        fun create(planId: Long): AddEditTransactionViewModel
    }

    private val _formState = MutableStateFlow(TransactionFormState())
    val formState = _formState.asStateFlow()

    var transactionResult by mutableStateOf<Result<Unit>?>(null)
        private set

    private val _editTransaction = MutableStateFlow(0L)
    val editTransaction = _editTransaction.onStart {
        val data = getTransactionUseCase(_editTransaction.value) ?: return@onStart
        _formState.value = formState.value.copy(
            to = data.transaction.to,
            note = data.transaction.note,
            amount = data.transaction.amount.toString(),
            images = data.billImages.map { img -> img.imageUrl.toUri() }.toSet()
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    fun setTransactionIdForEdit(transactionId: Long) {
        _editTransaction.value = transactionId
    }

    private suspend fun isTransactionDataIsValid(): Boolean {

        val fieldValidationResults = validateTransactionFormUseCase(
            _formState.value.to,
            _formState.value.amount,
            _formState.value.note,
            _formState.value.images
        )

        // If any field validation fails, return immediately
        if (fieldValidationResults.any { it.value is ValidationResult.Error }) {
            _formState.value = formState.value.copy(validationResult = fieldValidationResults)

            return false
        }

        // Step 2: Fetch Business Rules (Budget & Max Date)
        val plan = getPlanUseCase(planId).getOrNull() ?: return false

        // Step 3: Validate Business Constraints
        val constraintResult = checkTransactionConstraintsUseCase(
            _formState.value.amount.toDouble(),
            plan.plan.initialBudget,
            _formState.value.date,
            System.currentTimeMillis()
        )

        val result = fieldValidationResults + (TransactionForm.Amount to constraintResult)
        _formState.value = formState.value.copy(validationResult = result)

        // Return success results
        return result.values.none { it is ValidationResult.Error }
    }

    fun addTransaction(
        planId: Long
    ) {
        viewModelScope.launch {

            if (!isTransactionDataIsValid()) return@launch

            val billImages = formState.value.images.map { it.toString() }
            val result = createTransactionUseCase(
                planId = planId,
                to = formState.value.to,
                note = formState.value.note,
                amount = formState.value.amount,
                transactionStatus = formState.value.transactionStatus,
                billImages
            )
            transactionResult = result
        }
    }

    fun onImagePickerResult(uri: List<Uri>) {
        _formState.value = formState.value.copy(images = uri.toSet() + formState.value.images)
    }

    fun removeSelectedBillImage(uri: Uri) {
        val images = formState.value.images - uri
        _formState.value = formState.value.copy(images = images)

    }

    fun onAmountValueChange(value: String) {
        _formState.value = _formState.value.copy(amount = value)
    }

    fun onToValueChange(value: String) {
        _formState.value = _formState.value.copy(to = value)
    }

    fun onNoteValueChange(value: String) {
        _formState.value = _formState.value.copy(note = value)
    }

    fun onDateValueChange(value: Long) {
        _formState.value = _formState.value.copy(date = value)
    }

    fun onTransactionStatusValueChange(value: TransactionStatus) {
        _formState.value = _formState.value.copy(transactionStatus = value)
    }

    fun onBudgetUnitChange(newUnit: BudgetUnit) {
        _formState.value = _formState.value.copy(selectedBudgetUnit = newUnit)
    }
}