package net.afanasev.otonfm.data.chat

import com.google.firebase.Timestamp

data class MessageModel(
    var id: String = "",
    var type: String = MessageType.USER_MESSAGE.value,
    var text: String = "",
    var authorId: String = "",
    var authorName: String = "",
    var authorFlag: String = "",
    @JvmField var isAuthorAdmin: Boolean = false,
    @JvmField var isAuthorPremium: Boolean = false,
    var songArtist: String? = null,
    var songTitle: String? = null,
    @JvmField var isHidden: Boolean = false,
    @JvmField var isPinned: Boolean = false,
    @JvmField var isUrgent: Boolean = false,
    var createdAt: Timestamp? = null,
)

enum class MessageType(val value: String) {
    USER_MESSAGE("userMessage"),
    SONG_REQUEST("songRequest"),
    ADMIN_ANNOUNCEMENT("adminAnnouncement"),
    SYSTEM("system");

    companion object {
        fun fromValue(value: String): MessageType =
            entries.firstOrNull { it.value == value } ?: USER_MESSAGE
    }
}
