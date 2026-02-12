package net.afanasev.otonfm

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface MainRoutes {
    @Serializable
    object Player : MainRoutes, NavKey

    @Serializable
    object Menu : MainRoutes, NavKey

    @Serializable
    object ThemeChooser : MainRoutes, NavKey

    @Serializable
    object Contacts : MainRoutes, NavKey
}
