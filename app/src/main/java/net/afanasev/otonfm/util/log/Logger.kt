package net.afanasev.otonfm.util.log

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

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