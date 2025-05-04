package net.afanasev.otonfm.player.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayButton(
    isPlaying: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        enabled = isEnabled,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color(0xFFCD0200),
            disabledContainerColor = Color(0xFF990200),
        ),
        modifier = Modifier
            .size(96.dp)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
    ) {
        Icon(
            if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            if (isPlaying) "Pause" else "Play",
            tint = Color.White,
            modifier = Modifier.size(54.dp),
        )
    }
}

@Preview
@Composable
fun PlayButtonDisabledPreview() {
    PlayButton(
        isPlaying = false,
        isEnabled = false,
    ) { }
}

@Preview
@Composable
fun PlayButtonPlayingPreview() {
    PlayButton(
        isPlaying = true,
        isEnabled = true,
    ) { }
}

@Preview
@Composable
fun PlayButtonPausedPreview() {
    PlayButton(
        isPlaying = false,
        isEnabled = true,
    ) { }
}