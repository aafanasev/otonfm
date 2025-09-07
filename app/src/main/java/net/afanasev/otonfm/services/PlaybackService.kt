package net.afanasev.otonfm.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import net.afanasev.otonfm.BuildConfig

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private val noisyReceiver = StopCommandReceiver(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private val sleepTimerReceiver = StopCommandReceiver(ACTION_SLEEP_TIMER)

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        registerReceiver(noisyReceiver, IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
        registerSleepTimerReceiver()
    }

    override fun onDestroy() {
        destroyMediaSession()
        unregisterReceiver(noisyReceiver)
        unregisterReceiver(sleepTimerReceiver)
        super.onDestroy()
    }


    private fun registerSleepTimerReceiver() {
        val filter = IntentFilter(ACTION_SLEEP_TIMER)
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(sleepTimerReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            @SuppressLint("UnspecifiedRegisterReceiverFlag")
            registerReceiver(sleepTimerReceiver, filter)
        }
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

    inner class StopCommandReceiver(private val action: String) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == action) {
                mediaSession?.let {
                    if (it.player.isPlaying) {
                        it.player.stop()
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_SLEEP_TIMER = "${BuildConfig.APPLICATION_ID}.ACTION_SLEEP_TIMER"
    }

}