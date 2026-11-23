package net.afanasev.otonfm.theme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.common.ui.TextRowItem
import net.afanasev.otonfm.theme.data.ThemeMode

@Composable
fun ThemeChooserScreen(onThemeSelected: (ThemeMode) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        val items = listOf(
            ThemeMode.ARTWORK to R.string.theme_artwork,
            ThemeMode.DARK to R.string.theme_dark,
            ThemeMode.LIGHT to R.string.theme_light,
            ThemeMode.SYSTEM to R.string.theme_system,
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
