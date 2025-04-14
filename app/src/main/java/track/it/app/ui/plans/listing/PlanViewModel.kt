package track.it.app.ui.plans.listing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import track.it.app.data.prefernces.PlanSortPreferencesManager
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanSortType
import track.it.app.domain.model.SortOrder
import track.it.app.domain.usecases.plan.GetPlansUseCase
import track.it.app.ui.model.SortOption
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val getPlansUseCase: GetPlansUseCase,
    private val sortPreferencesManager: PlanSortPreferencesManager
) : ViewModel() {
    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    // Fetch the active sort preferences from the preferences manager
    val sortOption: StateFlow<SortOption> = combine(
        sortPreferencesManager.sortByFlow,
        sortPreferencesManager.sortOrderFlow
    ) { sortBy, sortOrder ->
        // Create a SortOption for UI based on preferences
        if (sortBy != null && sortOrder != null) {
            Log.d("PlanView", "ActiveSort: $sortBy$sortOrder")
            SortOption.ActiveSort(PlanSortType.valueOf(sortBy), SortOrder.valueOf(sortOrder))
        } else {
            Log.d("PlanView", "noSort: $sortBy$sortOrder")
            SortOption.NoSort
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SortOption.NoSort)

    // Combine sortOption UI state with the preferences to determine the active sort
    @OptIn(ExperimentalCoroutinesApi::class)
    val plansFlow: Flow<PagingData<PlanDetails>> = combine(
        _queryFlow,
        sortOption
    ) { search, preferencesSortOption ->
        // Map the UI state to domain models for the use case
        val (sortType, sortOrder) = when (val pref = sortOption.value) {
            is SortOption.ActiveSort -> Pair(pref.type, pref.order)
            SortOption.NoSort -> Pair(null, null)
        }

        // Fetch the plans using the domain models
        getPlansUseCase(search, sortType, sortOrder)
    }.flatMapLatest { it } // Unwrap the flow from the use case

    // Method to update the sort option when the user interacts with the UI
    fun updateSortOption(sortOption: SortOption) {
        viewModelScope.launch(Dispatchers.IO) {
            when (sortOption) {
                is SortOption.ActiveSort -> {
                    sortPreferencesManager.saveSortOptions(
                        sortOption.type.name,
                        sortOption.order.name
                    )
                }

                SortOption.NoSort -> {
                    sortPreferencesManager.clearSortOption()
                }
            }
        }
    }


    fun updateQuery(query: String) {
        _queryFlow.value = query
    }
}