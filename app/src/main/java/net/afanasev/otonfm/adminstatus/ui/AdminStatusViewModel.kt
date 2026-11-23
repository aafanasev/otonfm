package net.afanasev.otonfm.adminstatus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import net.afanasev.otonfm.adminstatus.data.AdminStatusModel
import net.afanasev.otonfm.adminstatus.data.AdminStatusRepository

class AdminStatusViewModel : ViewModel() {

    private val adminStatusRepository = AdminStatusRepository()

    // Holding the stream here keeps a single Firestore listener alive across
    // recompositions and configuration changes.
    val adminStatus: StateFlow<AdminStatusModel?> = adminStatusRepository.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
