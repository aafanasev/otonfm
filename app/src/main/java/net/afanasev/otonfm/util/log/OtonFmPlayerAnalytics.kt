package net.afanasev.otonfm.util.log

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import net.afanasev.radioplayer.core.analytics.PlayerAnalytics
import net.afanasev.radioplayer.core.player.PlayerButtonState
import net.afanasev.radioplayer.core.theme.PlayerTheme

private const val TAG = "OtonFmPlayerAnalytics"

object OtonFmPlayerAnalytics : PlayerAnalytics {

    private val analytics = Firebase.analytics

    override fun onPlayButtonClick(state: PlayerButtonState) {
        analytics.logEvent("play_btn_click", Bundle().apply {
            putString("state", state.name)
        })
    }

    override fun onThemeSelect(theme: PlayerTheme) {
        analytics.logEvent("theme_select", Bundle().apply {
            putString("theme", theme.name)
        })
    }

    override fun onMetadataFetchError(source: String, throwable: Throwable) {
        Log.e(TAG, "Cannot load $source", throwable)

        analytics.logEvent("metadata_fetch_error", Bundle().apply {
            putString("source", source)
            putString("type", throwable.javaClass.simpleName)
            putString("msg", throwable.message)
        })
    }

    override fun onArtworkMismatch(attempt: Int, maxAttempts: Int, retryDelayMs: Long) {
        Log.w(TAG, "Current track and status mismatch ($attempt of $maxAttempts). Wait $retryDelayMs ms.")

        analytics.logEvent("artwork_mismatch", Bundle().apply {
            putInt("attempt", attempt)
            putInt("max_count", maxAttempts)
            putLong("delay", retryDelayMs)
        })
    }
}
