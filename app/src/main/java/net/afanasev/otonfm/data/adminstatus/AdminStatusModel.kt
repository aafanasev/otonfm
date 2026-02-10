package net.afanasev.otonfm.data.adminstatus

import com.google.firebase.Timestamp

data class AdminStatusModel(
    var text: String = "",
    var type: String = "normal",
    @JvmField var isActive: Boolean = false,
    var updatedAt: Timestamp = Timestamp.now(),
) {
    val isUrgent: Boolean get() = type == "urgent"
}
