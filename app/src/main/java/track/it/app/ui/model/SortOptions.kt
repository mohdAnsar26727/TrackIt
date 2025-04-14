package track.it.app.ui.model

import android.content.Context
import track.it.app.R
import track.it.app.domain.model.PlanSortType
import track.it.app.domain.model.SortOrder

sealed class SortOption {
    data class ActiveSort(
        val type: PlanSortType,
        val order: SortOrder
    ) : SortOption()

    data object NoSort : SortOption() // Represents the state where no sort is applied
}

fun PlanSortType.toDisplayLabel(context: Context): String {
    return when (this) {
        PlanSortType.CREATED_AT -> context.getString(R.string.sort_created_at)
        PlanSortType.UPDATED_AT -> context.getString(R.string.sort_updated_at)
        PlanSortType.TITLE -> context.getString(R.string.title)
        PlanSortType.BUDGET -> context.getString(R.string.budget)
    }
}
