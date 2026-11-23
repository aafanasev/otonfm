package net.afanasev.otonfm.common.log

import android.os.Bundle
import android.util.Log
import androidx.media3.common.PlaybackException
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics

object Logger {

    private const val TAG = "Logger"
    private val analytics = Firebase.analytics

    //region UI events

    fun onMenuClick() {
        analytics.logEvent("menu_click", null)
    }

    fun onMenuThemeClick() {
        analytics.logEvent("menu_theme_click", null)
    }

    fun onMenuContactsClick() {
        analytics.logEvent("menu_contacts_click", null)
    }

    fun onThemeSelect(theme: String) {
        analytics.logEvent("theme_select", Bundle().apply {
            putString("theme", theme)
        })
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

    fun logAdminStatusError(message: String) {
        Log.e(TAG, "Admin status error: $message")
        analytics.logEvent("admin_status_error", Bundle().apply {
            putString("msg", message)
        })
    }

    //endregion

    //region Player

    /** @param state the playback state when the button was clicked */
    fun onPlayButtonClick(state: String) {
        analytics.logEvent("play_btn_click", Bundle().apply {
            putString("state", state)
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

    /** @param cause null when the stream timed out */
    fun onPlaybackError(reason: String, cause: Throwable?) {
        Log.e(TAG, "Playback error: $reason", cause)

        analytics.logEvent("playback_error", Bundle().apply {
            putString("reason", reason)
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

    //endregion

    //region Chat

    fun onChatButtonClick() {
        analytics.logEvent("chat_btn_click", null)
    }

    fun onChatSignIn() {
        analytics.logEvent("chat_sign_in", null)
    }

    fun onChatMessageSend() {
        analytics.logEvent("chat_message_send", null)
    }

    fun logChatError(message: String) {
        Log.e(TAG, "Chat error: $message")
        analytics.logEvent("chat_error", Bundle().apply {
            putString("msg", message)
        })
    }

    //endregion

}