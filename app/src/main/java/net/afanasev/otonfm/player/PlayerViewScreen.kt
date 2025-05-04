package net.afanasev.otonfm.player

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import net.afanasev.otonfm.player.components.Artwork
import net.afanasev.otonfm.player.components.Logo
import net.afanasev.otonfm.player.components.PlayButton
import net.afanasev.otonfm.player.components.Title

@Composable
fun PlayerViewScreen(
    viewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val controller = viewModel.controller
    val configuration = LocalConfiguration.current

    var isPlaying by remember { mutableStateOf(false) }
    var isChangingState by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    val artwork = viewModel.artworkUriFlow.collectAsStateWithLifecycle()

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

    val onPlayButtonClick = {
        isChangingState = true
        viewModel.playPause()
    }

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .padding(20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Artwork(
                    artworkUri = artwork.value,
                    modifier = Modifier.fillMaxHeight(0.8f),
                )
            }
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Logo(modifier = Modifier.fillMaxWidth(0.6f))
                Spacer(modifier = Modifier.height(24.dp))
                Title(
                    text = title,
                    modifier = Modifier.fillMaxWidth(0.8f),
                )
                Spacer(modifier = Modifier.height(24.dp))
                PlayButton(
                    isPlaying = isPlaying,
                    isEnabled = !isChangingState,
                    onClick = onPlayButtonClick,
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Logo(modifier = Modifier.fillMaxWidth(0.6f))
            Spacer(modifier = Modifier.height(36.dp))
            Artwork(
                artworkUri = artwork.value,
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Title(
                text = title,
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Spacer(modifier = Modifier.height(36.dp))
            PlayButton(
                isPlaying = isPlaying,
                isEnabled = !isChangingState,
                onClick = onPlayButtonClick,
            )
        }
    }
}

private fun CharSequence?.orEmpty(): String = this?.toString() ?: ""