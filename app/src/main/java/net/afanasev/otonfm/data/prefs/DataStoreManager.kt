package net.afanasev.otonfm.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    private companion object {
        val KEY_LAST_PROFILE_UPDATE = longPreferencesKey("last_profile_update_at")
    }

    val lastProfileUpdateAt: Flow<Long> =
        context.dataStore.data.map { prefs -> prefs[KEY_LAST_PROFILE_UPDATE] ?: 0L }

    suspend fun saveLastProfileUpdateAt(time: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_PROFILE_UPDATE] = time
        }
    }

}
