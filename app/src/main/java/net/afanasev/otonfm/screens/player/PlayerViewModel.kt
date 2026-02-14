package net.afanasev.otonfm.screens.player

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import net.afanasev.otonfm.data.adminstatus.AdminStatusFetcher
import net.afanasev.otonfm.data.adminstatus.AdminStatusModel
import net.afanasev.otonfm.data.status.DEFAULT_ARTWORK_URI
import net.afanasev.otonfm.data.status.StatusFetcher
import net.afanasev.otonfm.services.PlaybackService

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val _adminStatus = MutableStateFlow<AdminStatusModel?>(AdminStatusModel())
    val adminStatus: StateFlow<AdminStatusModel?> = _adminStatus.asStateFlow()

    private val _artworkUri = MutableStateFlow<String>(DEFAULT_ARTWORK_URI)
    val artworkUri: StateFlow<String> = _artworkUri.asStateFlow()

    private val _title = MutableStateFlow<String>("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _nextTrackTitle = MutableStateFlow<String>("")
    val nextTrackTitle: StateFlow<String> = _nextTrackTitle.asStateFlow()

    private val _buttonState = MutableStateFlow<ButtonState>(ButtonState.PAUSED)
    val buttonState: StateFlow<ButtonState> = _buttonState.asStateFlow()

    private val adminStatusFetcher = AdminStatusFetcher()
    private val statusFetcher = StatusFetcher()
    private var mediaController: MediaController? = null

    init {
        adminStatusFetcher.observe()
            .onEach { _adminStatus.value = it }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val token =
                SessionToken(application, ComponentName(application, PlaybackService::class.java))

            mediaController =
                MediaController.Builder(application, token).buildAsync().await().also {
                    if (it.isPlaying) {
                        _buttonState.value = ButtonState.PLAYING
                        setMetadata(it.mediaMetadata)
                    }

                    it.addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(playing: Boolean) {
                            _buttonState.value =
                                if (playing) ButtonState.PLAYING else ButtonState.PAUSED

                        }

                        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                            setMetadata(mediaMetadata)
                        }
                    })
                }
        }
    }

    fun playPause() {
        if (_buttonState.value == ButtonState.PAUSED) {
            _buttonState.value = ButtonState.LOADING
        }

        mediaController?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                val mediaItem = MediaItem.fromUri("https://s4.radio.co/s696f24a77/listen")
                it.setMediaItem(mediaItem)
                it.prepare()
                it.play()
            }
        }
    }

    private fun setMetadata(mediaMetadata: MediaMetadata) {
        val currentTitle = mediaMetadata.title?.toString() ?: return
        _title.value = currentTitle

        viewModelScope.launch {
            _artworkUri.value = statusFetcher.fetchArtworkUri(currentTitle)
        }

        viewModelScope.launch {
            _nextTrackTitle.value = statusFetcher.fetchNextTrack().orEmpty()
        }
    }

    enum class ButtonState {
        PAUSED,
        LOADING,
        PLAYING,
    }
}
