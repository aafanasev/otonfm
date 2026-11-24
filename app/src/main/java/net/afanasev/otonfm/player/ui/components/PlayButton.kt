package net.afanasev.otonfm.player.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.player.fsm.Playback

enum class PlayerButtonState {
    PAUSED,
    LOADING,
    PLAYING,
}

/**
 * Call this before the state reaches Compose, as PlayerViewModel does: a `StateFlow` of the result
 * drops equal values, so moving between playback states that look the same doesn't recompose.
 */
fun Playback.toButtonState(): PlayerButtonState = when (this) {
    Playback.Connecting,
    Playback.Buffering,
    is Playback.Reconnecting -> PlayerButtonState.LOADING

    Playback.Idle,
    is Playback.Error -> PlayerButtonState.PAUSED

    Playback.Playing -> PlayerButtonState.PLAYING
}

/**
 * Always clickable: a tap while [PlayerButtonState.LOADING] cancels connecting or reconnecting.
 */
@Composable
fun PlayButton(
    buttonState: PlayerButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFCD0200)),
        modifier = modifier
            .size(96.dp)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
    ) {
        if (buttonState == PlayerButtonState.LOADING) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(54.dp),
            )
        } else {
            val icon: ImageVector
            val desc: String
            when (buttonState) {
                PlayerButtonState.PAUSED -> {
                    icon = Icons.Filled.PlayArrow
                    desc = "Play"
                }

                PlayerButtonState.PLAYING -> {
                    icon = Icons.Filled.Pause
                    desc = "Pause"
                }

                PlayerButtonState.LOADING -> throw IllegalStateException()
            }

            Icon(
                imageVector = icon,
                contentDescription = desc,
                tint = Color.White,
                modifier = Modifier.size(54.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PlayButtonLoadingPreview() {
    PlayButton(buttonState = PlayerButtonState.LOADING, onClick = {})
}

@Preview
@Composable
private fun PlayButtonPlayingPreview() {
    PlayButton(buttonState = PlayerButtonState.PLAYING, onClick = {})
}

@Preview
@Composable
private fun PlayButtonPausedPreview() {
    PlayButton(buttonState = PlayerButtonState.PAUSED, onClick = {})
}
