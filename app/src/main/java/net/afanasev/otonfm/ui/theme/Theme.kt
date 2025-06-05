package net.afanasev.otonfm.ui.theme

import androidx.annotation.StringDef
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    background = Color.Black,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    background = Color.LightGray,

    /* Other default colors to override

    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Retention(AnnotationRetention.SOURCE)
@StringDef(Theme.DARK, Theme.LIGHT, Theme.SYSTEM)
annotation class Theme {
    companion object {
        const val DARK = "dark"
        const val LIGHT = "light"
        const val SYSTEM = "system"
    }
}

@Composable
fun OtonFmTheme(
    @Theme theme: String,
    content: @Composable () -> Unit
) {
    val darkTheme = when (theme) {
        Theme.DARK -> true
        Theme.LIGHT -> false
        else -> isSystemInDarkTheme()
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val customColorsPalette = if (darkTheme) DarkCustomColorsPalette else LightCustomColorsPalette

    CompositionLocalProvider(LocalCustomColorsPalette provides customColorsPalette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}