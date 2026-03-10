package net.afanasev.otonfm.data.status

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val STATION_NAME = "Oton FM"
const val STATION_STREAM_URL = "https://s4.radio.co/s696f24a77/listen"
const val DEFAULT_ARTWORK_URI =
    "https://images.radio.co/station_logos/s696f24a77.20250505065303.jpg"

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
