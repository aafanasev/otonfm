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
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.afanasev.otonfm.MainActivity
import net.afanasev.otonfm.data.status.StatusFetcher

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var artworkJob: Job? = null
    private var lastFetchedTitle: String? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val statusFetcher = StatusFetcher()
    private val noisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // stop playing music when become noisy (e.g. unplug headphones)
            if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                mediaSession?.let {
                    if (it.player.isPlaying) {
                        it.player.stop()
                    }
                }
            }
        }

    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        registerReceiver(noisyReceiver, IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player ?: return
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
                    val uri = statusFetcher.fetchArtworkUri(title)
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

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivity)
            .build()
    }

    private fun destroyMediaSession() {
        artworkJob?.cancel()
        serviceScope.cancel()
        mediaSession?.let {
            it.player.release()
            it.release()
            mediaSession = null
        }
    }
}