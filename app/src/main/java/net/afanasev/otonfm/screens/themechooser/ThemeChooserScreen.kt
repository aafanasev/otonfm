package net.afanasev.otonfm.screens.themechooser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.theme.Theme

@Composable
fun ThemeChooserScreen(onThemeSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            val items = listOf(
                Theme.SYSTEM to R.string.theme_system,
                Theme.DARK to R.string.theme_dark,
                Theme.LIGHT to R.string.theme_light,
            )

            items.forEachIndexed { index, (value, stringResId) ->
                Text(
                    stringResource(stringResId),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable(onClick = { onThemeSelected(value) }),
                )

                if (index < items.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview
@Composable
fun ThemeChooserScreenPreview() {
    ThemeChooserScreen(onThemeSelected = {})
}