package net.afanasev.otonfm.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.afanasev.otonfm.ui.theme.Theme

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    private companion object {
        val KEY_THEME = stringPreferencesKey("theme")
    }

    val theme: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[KEY_THEME] ?: Theme.ARTWORK }

    suspend fun saveTheme(@Theme theme: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = theme
        }
    }

}