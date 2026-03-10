package net.afanasev.otonfm.data.status

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class StatusModel(
    @SerialName("current_track")
    val currentTrack: TrackModel,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class NextTrackModel(
    @SerialName("next_track")
    val nextTrack: TrackModel,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class TrackModel(
    @SerialName("title")
    val title: String,
    @SerialName("artwork_url_large")
    val artworkUri: String?,
)
