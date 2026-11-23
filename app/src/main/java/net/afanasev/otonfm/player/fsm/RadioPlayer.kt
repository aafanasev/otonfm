package net.afanasev.otonfm.player.fsm

import android.content.Context
import android.util.Log
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.afanasev.otonfm.R
import net.afanasev.otonfm.common.log.Logger
import net.afanasev.otonfm.player.data.RadioCoMetadataProvider

private const val RADIO_CO_STATION_ID = "s696f24a77"

/**
 * App-scoped player state machine. It outlives any screen, so playback changes made from the
 * notification, Bluetooth or Android Auto are tracked even with no UI alive.
 *
 * Observe [state] and call [send]; get the instance with [RadioPlayer.get]. Events are
 * reduced one at a time on the main thread (Media3 requires it), which also serialises them.
 *
 * It doesn't start the service: [net.afanasev.otonfm.player.ui.PlayerViewModel] does on
 * the first play, and the service then [attach]es its player.
 */
class RadioPlayer internal constructor(
    private val policy: PlayerPolicy,
    scope: CoroutineScope,
    effectsFactory: (send: (PlayerEvent) -> Unit) -> PlayerEffects,
) {

    private val _state = MutableStateFlow(PlayerState())

    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private val events = Channel<PlayerEvent>(Channel.UNLIMITED)
    private val effects = effectsFactory(::send)

    private val playerListener: Player.Listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.containsAny(
                    Player.EVENT_PLAY_WHEN_READY_CHANGED,
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_IS_PLAYING_CHANGED,
                    Player.EVENT_PLAYER_ERROR,
                )
            ) {
                // Report an error only in the batch that raised it, see PlayerSnapshot.error.
                val newError = player.playerError.takeIf { events.contains(Player.EVENT_PLAYER_ERROR) }
                send(PlayerEvent.PlayerChanged(player.toSnapshot(error = newError)))
            }
            if (events.contains(Player.EVENT_MEDIA_METADATA_CHANGED)) {
                send(PlayerEvent.MetadataChanged(player.mediaMetadata.toTrackMetadata()))
            }
        }
    }

    init {
        scope.launch {
            for (event in events) {
                val before = _state.value
                val next = reduce(before, event, policy)
                log(event, before, next)
                _state.value = next.state
                next.effects.forEach(effects::handle)
            }
        }
    }

    fun send(event: PlayerEvent) {
        events.trySend(event)
    }

    /**
     * Called by [net.afanasev.otonfm.player.service.PlaybackService] once its player is built:
     * player commands run on it from now on, and its changes feed the state machine.
     */
    internal fun attach(player: Player) {
        player.addListener(playerListener)
        effects.setPlayer(player)
    }

    /** Called by the service before it releases [player]. */
    internal fun detach(player: Player) {
        player.removeListener(playerListener)
        effects.setPlayer(null)
        send(PlayerEvent.ServiceDestroyed)
    }

    /**
     * Logs each event that changes the state or produces effects, e.g.
     * `PlayPauseClicked: Idle -> Connecting [StartPlayback(...), ...]`. Off by default; enable with
     * `adb shell setprop log.tag.RadioPlayer DEBUG`.
     */
    private fun log(event: PlayerEvent, before: PlayerState, next: Next) {
        if (!Log.isLoggable(TAG, Log.DEBUG)) return
        if (next.state == before && next.effects.isEmpty()) return

        val playback = if (next.state.playback == before.playback) {
            "${before.playback}"
        } else {
            "${before.playback} -> ${next.state.playback}"
        }
        val nowPlaying = if (next.state.nowPlaying != before.nowPlaying) " ${next.state.nowPlaying}" else ""
        val effects = if (next.effects.isEmpty()) "" else " ${next.effects}"
        Log.d(TAG, "$event: $playback$nowPlaying$effects")
    }

    companion object {
        private const val TAG = "RadioPlayer"

        @Volatile
        private var instance: RadioPlayer? = null

        /** Returns the app-wide instance. */
        fun get(context: Context): RadioPlayer =
            instance ?: synchronized(this) {
                instance ?: create(context.applicationContext).also { instance = it }
            }

        private fun create(context: Context): RadioPlayer {
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
            return RadioPlayer(
                policy = PlayerPolicy(streamUrl = context.getString(R.string.stream_url)),
                scope = scope,
            ) { send ->
                PlayerEffectHandler(
                    metadata = RadioCoMetadataProvider(
                        stationId = RADIO_CO_STATION_ID,
                        onFetchError = Logger::onMetadataFetchError,
                        onArtworkMismatch = Logger::onArtworkMismatch,
                    ),
                    stationName = context.getString(R.string.app_name),
                    scope = scope,
                    send = send,
                )
            }
        }
    }
}
