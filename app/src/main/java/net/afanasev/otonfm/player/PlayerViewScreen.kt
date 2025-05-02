package net.afanasev.otonfm.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import net.afanasev.otonfm.R

@Composable
fun PlayerViewScreen(
    viewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val controller = viewModel.controller

    var isPlaying by remember { mutableStateOf(false) }
    var isChangingState by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    LaunchedEffect(controller) {
        controller?.let {
            isPlaying = it.isPlaying
            title = it.mediaMetadata.title.orEmpty()

            it.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                    isChangingState = false
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    title = mediaMetadata.title.orEmpty()
                }
            })
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App logo"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // preview

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = title)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = !isChangingState,
            onClick = {
                isChangingState = true
                viewModel.playPause()
            },
        ) {
            Image(
                painter = painterResource(
                    id = if (isPlaying) {
                        R.drawable.baseline_pause_24
                    } else {
                        R.drawable.baseline_play_arrow_24
                    }
                ),
                contentDescription = if (isPlaying) "Pause" else "Play"
            )
        }
    }
}

private fun CharSequence?.orEmpty(): String = this?.toString() ?: ""