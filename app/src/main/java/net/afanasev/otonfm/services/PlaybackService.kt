package net.afanasev.otonfm.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.afanasev.otonfm.MainActivity
import net.afanasev.otonfm.OtonFmApplication
import net.afanasev.otonfm.data.status.DEFAULT_ARTWORK_URI
import net.afanasev.otonfm.data.status.STATION_NAME
import net.afanasev.otonfm.data.status.STATION_STREAM_URL

private const val ROOT_ID = "root"
private const val STATION_ID = "oton_fm_station"

class PlaybackService : MediaLibraryService() {

    private var mediaLibrarySession: MediaLibrarySession? = null
    private var artworkJob: Job? = null
    private var lastFetchedTitle: String? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val noisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // stop playing music when become noisy (e.g. unplug headphones)
            if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                mediaLibrarySession?.let {
                    if (it.player.isPlaying) {
                        it.player.stop()
                    }
                }
            }
        }
    }

    private val libraryCallback = object : MediaLibrarySession.Callback {
        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?,
        ): ListenableFuture<LibraryResult<MediaItem>> {
            val root = MediaItem.Builder()
                .setMediaId(ROOT_ID)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setIsBrowsable(true)
                        .setIsPlayable(false)
                        .build()
                )
                .build()
            return Futures.immediateFuture(LibraryResult.ofItem(root, params))
        }

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?,
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            if (parentId != ROOT_ID) {
                return Futures.immediateFuture(
                    LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
                )
            }
            val stationItem = MediaItem.Builder()
                .setMediaId(STATION_ID)
                .setUri(STATION_STREAM_URL)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(STATION_NAME)
                        .setArtworkUri(DEFAULT_ARTWORK_URI.toUri())
                        .setIsBrowsable(false)
                        .setIsPlayable(true)
                        .build()
                )
                .build()
            return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.of(stationItem), params))
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? =
        mediaLibrarySession

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        registerReceiver(noisyReceiver, IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaLibrarySession?.player ?: return
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        destroyMediaSession()
        unregisterReceiver(noisyReceiver)
        super.onDestroy()
    }

    private fun initMediaSession() {
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true, /* handleAudioFocus */
            )
            .build()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_IDLE) {
                    lastFetchedTitle = null
                }
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                val title = mediaMetadata.title?.toString()
                if (title == null || title == lastFetchedTitle) return
                lastFetchedTitle = title

                artworkJob?.cancel()
                artworkJob = serviceScope.launch {
                    val uri = (application as OtonFmApplication).statusRepository.fetchArtworkUri(title)
                    val currentItem = player.currentMediaItem ?: return@launch
                    val updatedMetadata = currentItem.mediaMetadata.buildUpon()
                        .setArtworkUri(uri.toUri())
                        .build()
                    player.replaceMediaItem(
                        player.currentMediaItemIndex,
                        currentItem.buildUpon().setMediaMetadata(updatedMetadata).build()
                    )
                }
            }
        })

        val sessionActivity = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        mediaLibrarySession = MediaLibrarySession.Builder(this, player, libraryCallback)
            .setSessionActivity(sessionActivity)
            .build()
    }

    private fun destroyMediaSession() {
        artworkJob?.cancel()
        serviceScope.cancel()
        mediaLibrarySession?.let {
            it.player.release()
            it.release()
            mediaLibrarySession = null
        }
    }
}
