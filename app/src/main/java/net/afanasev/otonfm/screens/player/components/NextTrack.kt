package net.afanasev.otonfm.screens.player.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import net.afanasev.otonfm.R

@Composable
fun NextTrack(
    text: String,
    modifier: Modifier,
) {
    Text(
        text = if (text.isEmpty()) "" else stringResource(R.string.player_next_track_prefix, text),
        textAlign = TextAlign.Center,
        minLines = 1,
        maxLines = 1,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.basicMarquee(iterations = Int.MAX_VALUE),
    )
}

@Preview
@Composable
fun NextTrackPreview() {
    NextTrack(
        text = "Michael Jackson - Billie Jean",
        modifier = Modifier,
    )
}