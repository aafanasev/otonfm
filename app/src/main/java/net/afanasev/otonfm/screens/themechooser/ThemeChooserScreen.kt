package net.afanasev.otonfm.screens.themechooser

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.afanasev.otonfm.R
import net.afanasev.otonfm.log.Logger
import net.afanasev.otonfm.ui.components.Dialog
import net.afanasev.otonfm.ui.components.DialogItem
import net.afanasev.otonfm.ui.theme.Theme

@Composable
fun ThemeChooserScreen(onThemeSelected: (String) -> Unit) {
    Dialog {
        val items = listOf(
            Theme.ARTWORK to R.string.theme_artwork,
            Theme.DARK to R.string.theme_dark,
            Theme.LIGHT to R.string.theme_light,
            Theme.SYSTEM to R.string.theme_system,
        )

        items.forEach { (value, stringResId) ->
            DialogItem(
                stringResId,
                onClick = {
                    Logger.onThemeSelect(value)
                    onThemeSelected(value)
                },
            )
        }
    }
}

@Preview
@Composable
fun ThemeChooserScreenPreview() {
    ThemeChooserScreen(onThemeSelected = {})
}