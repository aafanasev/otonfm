package net.afanasev.otonfm.log

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import net.afanasev.otonfm.screens.player.PlayerViewModel
import net.afanasev.otonfm.ui.theme.Theme

object Logger {

    private const val TAG = "Logger"
    private val analytics = Firebase.analytics

    //region UI events

    fun onPlayButtonClick(state: PlayerViewModel.ButtonState) {
        analytics.logEvent("play_btn_click", Bundle().apply {
            putString("state", state.name)
        })
    }

    fun onMenuClick() {
        analytics.logEvent("menu_click", null)
    }

    fun onMenuThemeClick() {
        analytics.logEvent("menu_theme_click", null)
    }

    fun onThemeSelect(@Theme theme: String) {
        analytics.logEvent("theme_select", Bundle().apply {
            putString("theme", theme)
        })
    }

    fun onMenuContactsClick() {
        analytics.logEvent("menu_contacts_click", null)
    }

    /** @param channel email, github, etc */
    fun onContactSelect(channel: String) {
        analytics.logEvent("contact_select", Bundle().apply {
            putString("channel", channel)
        })

    }

    //endregion

    //region Exceptions

    fun logMissingEmailClient() {
        Log.e(TAG, "No email client found")

        analytics.logEvent("no_email_client", null)
    }

    fun logStatusException(e: Exception) {
        Log.e(TAG, "Cannot load status", e)

        analytics.logEvent("status_exception", Bundle().apply {
            putString("type", e.javaClass.simpleName)
            putString("msg", e.message)
        })
    }

    fun logArtworkMismatch(attempt: Int, retryMaxCount: Int, retryDelayMs: Long) {
        Log.w(
            TAG,
            "Current track and status mismatch ($attempt of $retryMaxCount). Wait $retryDelayMs ms."
        )

        analytics.logEvent("artwork_mismatch", Bundle().apply {
            putInt("attempt", attempt)
            putInt("max_count", retryMaxCount)
            putLong("delay", retryDelayMs)
        })
    }

    fun logNextTrackException(e: Exception) {
        Log.e(TAG, "Cannot load next track", e)

        analytics.logEvent("next_track_exception", Bundle().apply {
            putString("type", e.javaClass.simpleName)
            putString("msg", e.message)
        })
    }

    fun logAdminStatusError(message: String) {
        Log.e(TAG, "Admin status error: $message")
        analytics.logEvent("admin_status_error", Bundle().apply {
            putString("msg", message)
        })
    }

    //endregion

}