package net.afanasev.radioplayer.core.player.fsm

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.afanasev.radioplayer.core.analytics.PlayerAnalytics
import net.afanasev.radioplayer.core.metadata.NowPlayingMetadataProvider
import net.afanasev.radioplayer.core.player.toButtonState

/** Runs [PlayerEffect]s. Split out from [RadioPlayer] so tests can fake it. */
internal interface PlayerEffects {
    fun handle(effect: PlayerEffect)

    /** The service's player, which runs the player commands; null while there is no service. */
    fun setPlayer(player: Player?)
}

internal class PlayerEffectHandler(
    private val metadata: NowPlayingMetadataProvider,
    private val analytics: PlayerAnalytics,
    private val scope: CoroutineScope,
    private val send: (PlayerEvent) -> Unit,
) : PlayerEffects {

    private var player: Player? = null

    /** The last player command sent before the service was up. A newer one replaces it. */
    private var pendingCommand: ((Player) -> Unit)? = null

    private var nextTrackJob: Job? = null
    private var timeoutJob: Job? = null
    private var retryJob: Job? = null

    override fun handle(effect: PlayerEffect) {
        when (effect) {
            // Order matters, see PlayerEffect.StartPlayback.
            is PlayerEffect.StartPlayback -> command {
                it.stop()
                it.setMediaItem(MediaItem.fromUri(effect.streamUrl))
                it.play()
                it.prepare()
            }

            PlayerEffect.StopPlayback -> command {
                it.pause()
                it.stop()
            }

            PlayerEffect.StopForRetry -> command { it.stop() }

            is PlayerEffect.FetchNextTrack -> {
                nextTrackJob?.cancel()
                nextTrackJob = scope.launch {
                    val next = try {
                        metadata.fetchNextTrack()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        analytics.onMetadataFetchError("next_track", e)
                        null
                    }
                    send(PlayerEvent.NextTrackLoaded(effect.forTitle, next))
                }
            }

            is PlayerEffect.StartConnectTimeout -> {
                timeoutJob?.cancel()
                timeoutJob = scope.launch {
                    delay(effect.millis)
                    send(PlayerEvent.ConnectTimedOut)
                }
            }

            PlayerEffect.CancelConnectTimeout -> {
                timeoutJob?.cancel()
                timeoutJob = null
            }

            is PlayerEffect.ScheduleRetry -> {
                retryJob?.cancel()
                retryJob = scope.launch {
                    delay(effect.delayMs)
                    send(PlayerEvent.RetryDue(effect.attempt))
                }
            }

            PlayerEffect.CancelRetry -> {
                retryJob?.cancel()
                retryJob = null
            }

            is PlayerEffect.TrackAnalytics -> when (val event = effect.event) {
                is AnalyticsEvent.PlayButtonClick -> analytics.onPlayButtonClick(event.playback.toButtonState())
                is AnalyticsEvent.PlaybackError -> analytics.onPlaybackError(event.reason, event.cause)
                is AnalyticsEvent.Reconnect -> analytics.onReconnect(event.attempt, event.delayMs)
            }
        }
    }

    override fun setPlayer(player: Player?) {
        this.player = player
        val pending = pendingCommand ?: return
        pendingCommand = null
        if (player != null) pending(player)
    }

    /**
     * Runs [block] on the service's player. Before the service is up (the first play starts it,
     * see PlayerViewModel) it waits for [setPlayer], so a quick play-then-stop doesn't play.
     */
    private fun command(block: (Player) -> Unit) {
        val player = player
        if (player != null) block(player) else pendingCommand = block
    }
}
