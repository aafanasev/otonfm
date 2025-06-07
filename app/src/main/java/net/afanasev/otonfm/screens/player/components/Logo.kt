package net.afanasev.otonfm.screens.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R

@Composable
fun Logo(
    isDarkMode: Boolean,
    modifier: Modifier,
) {
    Image(
        painter = painterResource(
            id = if (isDarkMode) {
                R.drawable.logo_dark
            } else {
                R.drawable.logo_light
            }
        ),
        contentDescription = "App logo",
        modifier = modifier,
    )
}

@Preview
@Composable
fun LogoPreviewLight() {
    Logo(
        isDarkMode = false,
        Modifier.width(60.dp),
    )
}

@Preview
@Composable
fun LogoPreviewDark() {
    Logo(
        isDarkMode = true,
        Modifier.width(60.dp),
    )
}