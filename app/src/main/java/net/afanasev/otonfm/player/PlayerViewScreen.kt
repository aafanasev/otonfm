package net.afanasev.otonfm.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import net.afanasev.otonfm.R
import java.nio.file.WatchEvent

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
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App logo",
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

//        Image(
//            painter = painterResource(id = R.drawable.baseline_audiotrack_24),
//            contentDescription = "Preview"
//        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = title)

        Spacer(modifier = Modifier.height(24.dp))

        IconButton(
            enabled = !isChangingState,
            onClick = {
                isChangingState = true
                viewModel.playPause()
            },
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
}

private fun CharSequence?.orEmpty(): String = this?.toString() ?: ""