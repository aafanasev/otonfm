package net.afanasev.otonfm.screens.player.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NextTrack(
    text: String,
    modifier: Modifier,
) {
    if (text.isNotEmpty()) {
        Text(
            text = "Next: $text",
            textAlign = TextAlign.Center,
            minLines = 1,
            maxLines = 2,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier,
        )
    }
}
