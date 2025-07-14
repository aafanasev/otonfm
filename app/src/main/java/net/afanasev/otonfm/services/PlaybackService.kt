package net.afanasev.otonfm.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
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
        mediaSession = MediaSession.Builder(this, player).build()
    }

    private fun destroyMediaSession() {
        mediaSession?.let {
            it.player.release()
            it.release()
            mediaSession = null
        }
    }
}