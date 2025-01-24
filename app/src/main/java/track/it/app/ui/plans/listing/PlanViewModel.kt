package track.it.app.ui.plans.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.usecases.plan.GetPlansUseCase
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val getPlansUseCase: GetPlansUseCase
) : ViewModel() {
    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    // Generates a PagingData flow whenever the query changes
    @OptIn(ExperimentalCoroutinesApi::class)
    val plansFlow: Flow<PagingData<PlanDetails>> = queryFlow
        .flatMapLatest { query ->
            getPlansUseCase(query).cachedIn(viewModelScope)
        }

    fun updateQuery(query: String) {
        _queryFlow.value = query
    }
}