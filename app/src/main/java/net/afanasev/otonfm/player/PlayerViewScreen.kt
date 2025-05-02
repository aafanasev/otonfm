package net.afanasev.otonfm.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.theme.LocalCustomColorsPalette

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

            if (isPlaying) {
                viewModel.fetchArtwork()
            }

            it.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                    isChangingState = false
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    title = mediaMetadata.title.orEmpty()
                    viewModel.fetchArtwork()
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
            modifier = Modifier.fillMaxWidth(0.6f)
        )

        Spacer(modifier = Modifier.height(36.dp))

        val previewShape = RoundedCornerShape(12.dp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                .shadow(
                    elevation = 6.dp,
                    shape = previewShape,
                    ambientColor = Color.DarkGray,
                    spotColor = Color.DarkGray
                )
                .clip(previewShape)
                .background(LocalCustomColorsPalette.current.previewBackground),
        ) {
            Icon(
                imageVector = Icons.Filled.Audiotrack,
                contentDescription = "Preview",
                tint = Color.LightGray,
                modifier = Modifier.size(96.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(0.8f),
        )

        Spacer(modifier = Modifier.height(36.dp))

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