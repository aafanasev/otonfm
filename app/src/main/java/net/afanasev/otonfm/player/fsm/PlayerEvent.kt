package net.afanasev.otonfm.player.fsm

import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player

sealed interface PlayerEvent {
    // user
    /** Play button tapped: starts from Idle or Error (retry), stops or cancels otherwise. */
    data object PlayPauseClicked : PlayerEvent

    // service
    /** The `MediaController` that starts the service on play failed to connect. */
    data class ControllerFailed(val cause: Throwable) : PlayerEvent

    /** The service and its player are gone, e.g. stopped after the app was swiped away. */
    data object ServiceDestroyed : PlayerEvent

    // Player.Listener
    data class PlayerChanged(val snapshot: PlayerSnapshot) : PlayerEvent
    data class MetadataChanged(val metadata: TrackMetadata) : PlayerEvent

    // effect results
    data class NextTrackLoaded(val forTitle: String, val nextTitle: String?) : PlayerEvent

    /** [artworkUri] is null when the provider had none or failed; the default artwork shows then. */
    data class ArtworkLoaded(val forTitle: String, val artworkUri: String?) : PlayerEvent
    data object ConnectTimedOut : PlayerEvent
    data class RetryDue(val attempt: Int) : PlayerEvent
}

/**
 * A consistent view of the player taken once per `Player.Listener.onEvents` batch.
 * [error] is set only in the batch where the error was raised, so a later batch that still
 * carries the same `Player.playerError` is not counted as a second failure.
 */
data class PlayerSnapshot(
    val playWhenReady: Boolean,
    /** One of `Player.STATE_*`. */
    val playbackState: Int,
    val isPlaying: Boolean,
    val error: PlaybackException? = null,
)

/**
 * What the stream itself reports. Artwork isn't part of it: it comes from the
 * [net.afanasev.otonfm.player.data.RadioCoMetadataProvider] via [PlayerEvent.ArtworkLoaded].
 */
data class TrackMetadata(val title: String?)

internal fun Player.toSnapshot(error: PlaybackException? = playerError) =
    PlayerSnapshot(
        playWhenReady = playWhenReady,
        playbackState = playbackState,
        isPlaying = isPlaying,
        error = error,
    )

internal fun MediaMetadata.toTrackMetadata() =
    TrackMetadata(title = title?.toString())
