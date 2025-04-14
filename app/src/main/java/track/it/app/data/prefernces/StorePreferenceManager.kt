package track.it.app.data.prefernces

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

@Singleton
class PlanSortPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val SORT_BY_KEY = stringPreferencesKey("sort_by")
        private val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
    }

    val sortByFlow: Flow<String?> = context.dataStore.data
        .map { it[SORT_BY_KEY] }

    val sortOrderFlow: Flow<String?> = context.dataStore.data
        .map { it[SORT_ORDER_KEY] }

    suspend fun saveSortOptions(sortBy: String, sortOrder: String) {
        context.dataStore.edit { prefs ->
            prefs[SORT_BY_KEY] = sortBy
            prefs[SORT_ORDER_KEY] = sortOrder
        }
    }

    suspend fun clearSortOption() {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }
}
