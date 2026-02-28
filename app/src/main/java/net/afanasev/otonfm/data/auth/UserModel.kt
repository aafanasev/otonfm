package net.afanasev.otonfm.data.auth

import com.google.firebase.Timestamp

data class UserModel(
    var displayName: String = "",
    var countryFlag: String = "",
    @JvmField var isAdmin: Boolean = false,
    @JvmField var isPremium: Boolean = false,
    var lastMessageAt: Timestamp? = null,
    var createdAt: Timestamp = Timestamp.now(),
)
