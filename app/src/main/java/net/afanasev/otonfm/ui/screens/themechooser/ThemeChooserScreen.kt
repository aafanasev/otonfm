package net.afanasev.otonfm.ui.screens.themechooser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.components.TextRowItem
import net.afanasev.radioplayer.core.theme.PlayerTheme

@Composable
fun ThemeChooserScreen(onThemeSelected: (PlayerTheme) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        val items = listOf(
            PlayerTheme.ARTWORK to R.string.theme_artwork,
            PlayerTheme.DARK to R.string.theme_dark,
            PlayerTheme.LIGHT to R.string.theme_light,
            PlayerTheme.SYSTEM to R.string.theme_system,
        )

        items.forEach { (value, stringResId) ->
            TextRowItem(
                stringResId,
                onClick = { onThemeSelected(value) },
            )
        }
    }
}

@Preview
@Composable
fun ThemeChooserScreenPreview() {
    ThemeChooserScreen(onThemeSelected = {})
}
