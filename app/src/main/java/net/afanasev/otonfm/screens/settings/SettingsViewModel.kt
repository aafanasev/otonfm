package net.afanasev.otonfm.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.afanasev.otonfm.data.prefs.DataStoreManager
import net.afanasev.otonfm.ui.theme.Theme

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = DataStoreManager(application)

    val theme: StateFlow<String> = dataStore.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, Theme.ARTWORK)

    val lastProfileUpdateAt: StateFlow<Long> = dataStore.lastProfileUpdateAt
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0L)

    fun saveTheme(@Theme theme: String) {
        viewModelScope.launch { dataStore.saveTheme(theme) }
    }

    fun saveLastProfileUpdateAt(time: Long) {
        viewModelScope.launch { dataStore.saveLastProfileUpdateAt(time) }
    }

    fun resetLastProfileUpdateAt() {
        saveLastProfileUpdateAt(0L)
    }
}
