package net.afanasev.otonfm.util.log

import android.os.Bundle
import android.util.Log
import androidx.media3.common.PlaybackException
import com.google.firebase.analytics.analytics
import com.google.firebase.Firebase
import net.afanasev.otonfm.core.player.PlayerButtonState
import net.afanasev.otonfm.core.player.fsm.ErrorReason
import net.afanasev.otonfm.data.theme.PlayerTheme

private const val TAG = "OtonFmPlayerAnalytics"

object OtonFmPlayerAnalytics {

    private val analytics = Firebase.analytics

    fun onPlayButtonClick(state: PlayerButtonState) {
        analytics.logEvent("play_btn_click", Bundle().apply {
            putString("state", state.name)
        })
    }

    fun onThemeSelect(theme: PlayerTheme) {
        analytics.logEvent("theme_select", Bundle().apply {
            putString("theme", theme.name)
        })
    }

    fun onMetadataFetchError(source: String, throwable: Throwable) {
        Log.e(TAG, "Cannot load $source", throwable)

        analytics.logEvent("metadata_fetch_error", Bundle().apply {
            putString("source", source)
            putString("type", throwable.javaClass.simpleName)
            putString("msg", throwable.message)
        })
    }

    fun onArtworkMismatch(attempt: Int, maxAttempts: Int, retryDelayMs: Long) {
        Log.w(TAG, "Current track and status mismatch ($attempt of $maxAttempts). Wait $retryDelayMs ms.")

        analytics.logEvent("artwork_mismatch", Bundle().apply {
            putInt("attempt", attempt)
            putInt("max_count", maxAttempts)
            putLong("delay", retryDelayMs)
        })
    }

    /** [cause] is null when the stream timed out. */
    fun onPlaybackError(reason: ErrorReason, cause: Throwable?) {
        Log.e(TAG, "Playback error: $reason", cause)

        analytics.logEvent("playback_error", Bundle().apply {
            putString("reason", reason.name)
            val type = (cause as? PlaybackException)?.errorCodeName ?: cause?.javaClass?.simpleName
            type?.let { putString("type", it) }
        })
    }

    fun onReconnect(attempt: Int, delayMs: Long) {
        Log.w(TAG, "Stream failed, reconnect attempt $attempt in $delayMs ms.")

        analytics.logEvent("playback_reconnect", Bundle().apply {
            putInt("attempt", attempt)
            putLong("delay", delayMs)
        })
    }
}
