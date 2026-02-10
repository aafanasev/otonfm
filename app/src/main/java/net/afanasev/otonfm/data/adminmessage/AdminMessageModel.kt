package net.afanasev.otonfm.data.adminmessage

data class AdminMessageModel(
    var text: String = "",
    var type: String = "normal",
    var isActive: Boolean = false,
    var updatedAt: Long = 0,
) {
    val isUrgent: Boolean get() = type == "urgent"
}
