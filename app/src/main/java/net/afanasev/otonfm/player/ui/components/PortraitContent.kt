package net.afanasev.otonfm.player.ui.components

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

@Composable
fun PortraitContent(
    title: TrackTitle,
    nextTrackTitle: String,
    buttonState: PlayerButtonState,
    artworkUri: String?,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    logo: @Composable () -> Unit = {},
    nextTrackPrefix: String = "Next",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        logo()
        Spacer(modifier = Modifier.height(24.dp))
        Artwork(
            artworkUri = artworkUri,
            modifier = Modifier.fillMaxWidth(0.9f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Title(
            title = title,
            modifier = Modifier.fillMaxWidth(0.9f),
        )
        Spacer(modifier = Modifier.height(12.dp))
        NextTrack(
            text = nextTrackTitle,
            modifier = Modifier.fillMaxWidth(0.9f),
            prefix = nextTrackPrefix,
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
private fun PortraitContentPreview() {
    PortraitContent(
        title = TrackTitle(song = "Teardrop", artist = "Massive Attack"),
        nextTrackTitle = "Portishead - Glory Box",
        buttonState = PlayerButtonState.PLAYING,
        artworkUri = null,
        onPlayClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PortraitContentPreviewLoading() {
    PortraitContent(
        title = TrackTitle(song = ""),
        nextTrackTitle = "",
        buttonState = PlayerButtonState.LOADING,
        artworkUri = null,
        onPlayClick = {},
    )
}
