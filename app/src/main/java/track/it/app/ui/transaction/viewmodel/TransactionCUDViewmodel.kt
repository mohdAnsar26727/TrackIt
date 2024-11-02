package track.it.app.ui.transaction.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import track.it.app.domain.model.Transaction
import track.it.app.domain.usecases.transaction.DeleteTransactionUseCase
import track.it.app.domain.usecases.transaction.UpdateTransactionUseCase
import javax.inject.Inject

@HiltViewModel
class TransactionCUDViewmodel @Inject constructor(
    val deleteTransactionUseCase: DeleteTransactionUseCase,
    val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    var transactionResult by mutableStateOf<Result<Unit>?>(null)
        private set

    fun updateTransaction(
        transaction: Transaction,
        billImages: List<String>
    ) {
        viewModelScope.launch {
            val transactionId = updateTransactionUseCase(transaction)

        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            transactionResult = deleteTransactionUseCase(id)
        }
    }

}