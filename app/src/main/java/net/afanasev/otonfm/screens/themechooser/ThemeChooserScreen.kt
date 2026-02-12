package net.afanasev.otonfm.screens.themechooser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.log.Logger
import net.afanasev.otonfm.ui.components.TextRowItem
import net.afanasev.otonfm.ui.theme.Theme

@Composable
fun ThemeChooserScreen(onThemeSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        val items = listOf(
            Theme.ARTWORK to R.string.theme_artwork,
            Theme.DARK to R.string.theme_dark,
            Theme.LIGHT to R.string.theme_light,
            Theme.SYSTEM to R.string.theme_system,
        )

        items.forEach { (value, stringResId) ->
            TextRowItem(
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
