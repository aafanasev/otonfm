package net.afanasev.otonfm.data

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class StatusModel(
    @SerialName("current_track")
    val currentTrack: CurrentTrack?,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class CurrentTrack(
    @SerialName("artwork_url_large")
    val artworkUri: String?,
)
