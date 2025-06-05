package net.afanasev.otonfm

import kotlinx.serialization.Serializable

sealed interface MainRoutes {
    @Serializable
    object Player : MainRoutes

    @Serializable
    object ThemeChooser : MainRoutes
}