package net.afanasev.otonfm.player

import android.app.Application
import android.content.ComponentName
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import net.afanasev.otonfm.data.StatusFetcher
import net.afanasev.otonfm.services.PlaybackService

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val statusFetcher = StatusFetcher()
    private val _artworkUriFlow = MutableStateFlow<String?>(null)

    var controller by mutableStateOf<MediaController?>(null)
        private set

    val artworkUriFlow: StateFlow<String?>
        get() = _artworkUriFlow

    init {
        viewModelScope.launch {
            val token =
                SessionToken(application, ComponentName(application, PlaybackService::class.java))
            controller = MediaController.Builder(application, token).buildAsync().await()
        }
    }

    fun playPause() {
        controller?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                if (it.currentMediaItem == null) {
                    val mediaItem = MediaItem.fromUri("https://s4.radio.co/s696f24a77/listen")
                    it.setMediaItem(mediaItem)
                    it.prepare()
                }
                it.play()
            }
        }
    }

    fun fetchArtwork() {
        viewModelScope.launch {
            _artworkUriFlow.value = statusFetcher.fetchArtworkUri()?.takeIf {
                // default image
                it != "https://images.radio.co/station_logos/s696f24a77.20250411103941.jpg"
            }
        }
    }

}