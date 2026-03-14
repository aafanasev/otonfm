package net.afanasev.otonfm.ui.screens.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.ui.screens.player.PlayerViewModel

@Composable
fun PortraitContent(
    title: String,
    nextTrackTitle: String,
    buttonState: PlayerViewModel.ButtonState,
    artworkUri: String,
    isDarkMode: Boolean,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Logo(
            isDarkMode = isDarkMode,
            modifier = Modifier.fillMaxWidth(0.6f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Artwork(
            artworkUri = artworkUri,
            modifier = Modifier.fillMaxWidth(0.9f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Title(
            text = title,
            modifier = Modifier.fillMaxWidth(0.9f),
        )
        Spacer(modifier = Modifier.height(6.dp))
        NextTrack(
            text = nextTrackTitle,
            modifier = Modifier.fillMaxWidth(0.9f),
        )
        Spacer(modifier = Modifier.height(32.dp))
        PlayButton(
            buttonState = buttonState,
            onClick = onPlayClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PortraitContentPreviewLight() {
    PortraitContent(
        title = "Massive Attack — Teardrop",
        nextTrackTitle = "Next: Portishead — Glory Box",
        buttonState = PlayerViewModel.ButtonState.PLAYING,
        artworkUri = "",
        isDarkMode = false,
        onPlayClick = {},
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PortraitContentPreviewDark() {
    PortraitContent(
        title = "Massive Attack — Teardrop",
        nextTrackTitle = "Next: Portishead — Glory Box",
        buttonState = PlayerViewModel.ButtonState.PAUSED,
        artworkUri = "",
        isDarkMode = true,
        onPlayClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PortraitContentPreviewLoading() {
    PortraitContent(
        title = "",
        nextTrackTitle = "",
        buttonState = PlayerViewModel.ButtonState.LOADING,
        artworkUri = "",
        isDarkMode = false,
        onPlayClick = {},
    )
}
