package net.afanasev.otonfm.data.adminstatus

import com.google.firebase.Timestamp

data class AdminStatusModel(
    var text: String = "",
    var type: String = TYPE_NORMAL,
    @JvmField var isActive: Boolean = false,
    var updatedAt: Timestamp = Timestamp.now(),
) {
    val isUrgent: Boolean get() = type == TYPE_URGENT

    companion object {
        const val TYPE_NORMAL = "normal"
        const val TYPE_URGENT = "urgent"
    }
}
