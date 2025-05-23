package net.afanasev.otonfm.player

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
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import net.afanasev.otonfm.data.StatusFetcher
import net.afanasev.otonfm.services.PlaybackService

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val _artworkUri = MutableStateFlow<String?>(null)
    val artworkUri: StateFlow<String?> = _artworkUri.asStateFlow()

    private val _title = MutableStateFlow<String>("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _isPlaying = MutableStateFlow<Boolean>(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isChangingState = MutableStateFlow<Boolean>(false)
    val isChangingState: StateFlow<Boolean> = _isChangingState.asStateFlow()

    private val statusFetcher = StatusFetcher()
    private var mediaController: MediaController? = null

    init {
        viewModelScope.launch {
            val token =
                SessionToken(application, ComponentName(application, PlaybackService::class.java))

            mediaController =
                MediaController.Builder(application, token).buildAsync().await().also {
                    if (it.isPlaying) {
                        _isPlaying.value = true
                        _title.value = it.mediaMetadata.title.orEmpty()
                        fetchArtwork()
                    }

                    it.addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(playing: Boolean) {
                            _isPlaying.value = playing
                            _isChangingState.value = false
                        }

                        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                            _title.value = mediaMetadata.title.orEmpty()
                            fetchArtwork()
                        }
                    })
                }
        }
    }

    fun playPause() {
        _isChangingState.value = true

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

    private fun fetchArtwork() {
        viewModelScope.launch {
            _artworkUri.value = statusFetcher.fetchArtworkUri()
        }
    }
}

private fun CharSequence?.orEmpty(): String = this?.toString() ?: ""