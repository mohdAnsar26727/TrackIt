package track.it.app.ui.transaction.create

import android.net.Uri
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import track.it.app.domain.model.TransactionStatus
import track.it.app.domain.usecases.transaction.CreateTransactionUseCase
import track.it.app.domain.usecases.transaction.GetTransactionUseCase
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionUseCase: GetTransactionUseCase
) : ViewModel() {

    var to by mutableStateOf("")
        private set
    var note by mutableStateOf("")
        private set
    var amount by mutableStateOf("")
        private set

    var selectedOption by mutableStateOf(TransactionStatus.PAID)
        private set

    private var selectedImages by mutableStateOf<Set<Uri>>(emptySet())
    val selectedImagesList by derivedStateOf { selectedImages.toList() }

    var transactionResult by mutableStateOf<Result<Unit>?>(null)
        private set

    private val _editTransaction = MutableStateFlow(0L)
    val editTransaction = _editTransaction.onStart {
        getTransactionUseCase(_editTransaction.value)?.let {
            with(it) {
                to = transaction.to
                note = transaction.note
                amount = transaction.amount.toString()
                selectedImages = billImages.mapNotNull { data -> Uri.parse(data.imageUrl) }.toSet()
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    fun setTransactionIdForEdit(transactionId: Long) {
        _editTransaction.value = transactionId
    }

    fun addTransaction(
        planId: Long
    ) {
        viewModelScope.launch {
            val billImages = selectedImages.map { it.toString() }
            val result = createTransactionUseCase(
                planId = planId,
                to,
                note,
                amount,
                selectedOption,
                billImages
            )
            transactionResult = result
        }
    }

    fun onImagePickerResult(uri: List<Uri>) {
        selectedImages = uri.toSet() + selectedImages
    }

    fun removeSelectedBillImage(uri: Uri) {
        selectedImages = selectedImages - uri
    }

    fun onAmountValueChange(value: String) {
        amount = value
    }

    fun onToValueChange(value: String) {
        to = value
    }

    fun onNoteValueChange(value: String) {
        note = value
    }

    fun onTransactionStatusValueChange(value: TransactionStatus) {
        selectedOption = value
    }
}