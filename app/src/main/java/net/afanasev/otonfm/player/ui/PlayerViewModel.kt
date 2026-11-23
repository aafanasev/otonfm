package net.afanasev.otonfm.player.ui

import android.app.Application
import android.content.ComponentName
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.afanasev.otonfm.R
import net.afanasev.otonfm.player.fsm.PlayerButtonState
import net.afanasev.otonfm.player.fsm.PlayerEvent
import net.afanasev.otonfm.player.fsm.PlayerState
import net.afanasev.otonfm.player.fsm.RadioPlayer
import net.afanasev.otonfm.player.fsm.toButtonState
import net.afanasev.otonfm.player.service.PlaybackService

/**
 * Adapts the app-scoped [RadioPlayer] state machine to UI flows. All playback logic lives in
 * the state machine; this only maps its state and forwards clicks.
 *
 * It also starts the [PlaybackService], lazily: the first play connects a `MediaController`,
 * which creates the service and keeps it bound until this view model is cleared. The service
 * then runs the play command, and Media3 keeps it running in the foreground while playing.
 */
class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val player = RadioPlayer.get(application)
    private var controllerFuture: ListenableFuture<MediaController>? = null

    private val defaultArtworkUri = application.getString(R.string.default_artwork_uri)

    val artworkUri: StateFlow<String> = derive { it.nowPlaying.artworkUri ?: defaultArtworkUri }

    val title: StateFlow<String> = derive { it.nowPlaying.title }

    val nextTrackTitle: StateFlow<String> = derive { it.nowPlaying.nextTrackTitle }

    val buttonState: StateFlow<PlayerButtonState> = derive { it.playback.toButtonState() }

    fun playPause() {
        connectService()
        player.send(PlayerEvent.PlayPauseClicked)
    }

    override fun onCleared() {
        controllerFuture?.let(MediaController::releaseFuture)
        controllerFuture = null
    }

    /** Creates and binds the service if not done yet; a no-op while connected. */
    private fun connectService() {
        if (controllerFuture != null) return

        val app = getApplication<Application>()
        val token = SessionToken(app, ComponentName(app, PlaybackService::class.java))
        lateinit var future: ListenableFuture<MediaController>
        future = MediaController.Builder(app, token)
            .setListener(object : MediaController.Listener {
                // The service was destroyed: connect again on the next play.
                override fun onDisconnected(controller: MediaController) {
                    if (controllerFuture === future) controllerFuture = null
                }
            })
            .buildAsync()
        controllerFuture = future

        Futures.addCallback(
            future,
            object : FutureCallback<MediaController> {
                override fun onSuccess(controller: MediaController) = Unit

                override fun onFailure(t: Throwable) {
                    if (controllerFuture !== future) return // released on purpose
                    controllerFuture = null
                    player.send(PlayerEvent.ControllerFailed(t))
                }
            },
            ContextCompat.getMainExecutor(app),
        )
    }

    /** Starts from the player's current state, so returning to the screen shows no empty frame. */
    private fun <T> derive(transform: (PlayerState) -> T): StateFlow<T> =
        player.state
            .map(transform)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), transform(player.state.value))
}
