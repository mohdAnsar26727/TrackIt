package track.it.app.ui.transaction.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import track.it.app.domain.model.TransactionWithImages
import track.it.app.domain.usecases.transaction.DeleteTransactionUseCase
import track.it.app.domain.usecases.transaction.GetTransactionUseCase

class TransactionDetailsViewModel @AssistedInject constructor(
    private val getTransactionUseCase: GetTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    @Assisted val transactionId: Long
) : ViewModel() {
    @AssistedFactory
    interface TransactionDetailsViewModelFactory {
        fun create(transactionId: Long): TransactionDetailsViewModel
    }

    private val _transactionById = MutableStateFlow<TransactionWithImages?>(null)
    val transactionById: StateFlow<TransactionWithImages?> = _transactionById
        .onStart {
            getTransactionById(transactionId)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    private fun getTransactionById(id: Long) {
        viewModelScope.launch {
            _transactionById.value = getTransactionUseCase(transactionId)
        }
    }
}