package net.afanasev.otonfm

import kotlinx.serialization.Serializable

sealed interface MainRoutes {
    @Serializable
    object Player : MainRoutes

    @Serializable
    object Settings : MainRoutes

    @Serializable
    object ThemeChooser : MainRoutes

    @Serializable
    object Contacts : MainRoutes
}