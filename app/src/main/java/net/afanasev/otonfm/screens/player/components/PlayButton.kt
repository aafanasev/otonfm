package net.afanasev.otonfm.screens.player.components

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.screens.player.PlayerViewModel

@Composable
fun PlayButton(
    buttonState: PlayerViewModel.ButtonState,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    IconButton(
        enabled = buttonState != PlayerViewModel.ButtonState.LOADING,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color(0xFFCD0200),
            disabledContainerColor = Color(0xFF660200),
        ),
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onLongClick() })
            }
            .size(96.dp)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
    ) {
        if (buttonState == PlayerViewModel.ButtonState.LOADING) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(54.dp),
            )
        } else {
            val icon: ImageVector
            val desc: String
            when (buttonState) {
                PlayerViewModel.ButtonState.PAUSED -> {
                    icon = Icons.Filled.PlayArrow
                    desc = "Play"
                }

                PlayerViewModel.ButtonState.PLAYING -> {
                    icon = Icons.Filled.Pause
                    desc = "Pause"
                }

                PlayerViewModel.ButtonState.LOADING -> throw IllegalStateException()
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
fun PlayButtonLoadingPreview() {
    PlayButton(
        buttonState = PlayerViewModel.ButtonState.LOADING,
        onClick = {},
        onLongClick = {},
    )
}

@Preview
@Composable
fun PlayButtonPlayingPreview() {
    PlayButton(
        buttonState = PlayerViewModel.ButtonState.PLAYING,
        onClick = {},
        onLongClick = {},
    )
}

@Preview
@Composable
fun PlayButtonPausedPreview() {
    PlayButton(
        buttonState = PlayerViewModel.ButtonState.PAUSED,
        onClick = {},
        onLongClick = {},
    )
}