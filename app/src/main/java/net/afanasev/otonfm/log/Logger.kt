package net.afanasev.otonfm.log

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import net.afanasev.otonfm.screens.player.PlayerViewModel
import net.afanasev.otonfm.ui.theme.Theme

object Logger {

    private val analytics = Firebase.analytics

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

}