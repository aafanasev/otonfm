package net.afanasev.otonfm.screens.player

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import net.afanasev.otonfm.data.status.DEFAULT_ARTWORK_URI
import net.afanasev.otonfm.screens.player.components.Artwork
import net.afanasev.otonfm.screens.player.components.Logo
import net.afanasev.otonfm.screens.player.components.PlayButton
import net.afanasev.otonfm.screens.player.components.Title
import net.afanasev.otonfm.ui.theme.Theme

@Composable
fun PlayerViewScreen(
    viewModel: PlayerViewModel,
    isDarkMode: Boolean,
    useArtworkAsBackground: Boolean,
    onArtworkLongClick: () -> Unit,
) {
    val artwork = viewModel.artworkUri.collectAsState()
    val title by viewModel.title.collectAsState()
    val buttonState by viewModel.buttonState.collectAsState()

    val configuration = LocalConfiguration.current
    val onPlayButtonClick = { viewModel.playPause() }

    if (useArtworkAsBackground) {
        if (artwork.value == DEFAULT_ARTWORK_URI) {
            // TODO: add gradient
        } else {
            AsyncImage(
                model = artwork.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(
                    Color.Black.copy(alpha = 0.3f),
                    BlendMode.Darken,
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .blur(20.dp),
            )
        }
    }

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize(),
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
                    onLongClick = onArtworkLongClick,
                    modifier = Modifier.fillMaxHeight(0.8f),
                )
            }
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Logo(
                    isDarkMode = isDarkMode,
                    modifier = Modifier.fillMaxWidth(0.6f),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Title(
                    text = title,
                    modifier = Modifier.fillMaxWidth(0.8f),
                )
                Spacer(modifier = Modifier.height(24.dp))
                PlayButton(
                    buttonState = buttonState,
                    onClick = onPlayButtonClick,
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Logo(
                isDarkMode = isDarkMode,
                modifier = Modifier.fillMaxWidth(0.6f),
            )
            Spacer(modifier = Modifier.height(36.dp))
            Artwork(
                artworkUri = artwork.value,
                onLongClick = onArtworkLongClick,
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Title(
                text = title,
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Spacer(modifier = Modifier.height(36.dp))
            PlayButton(
                buttonState = buttonState,
                onClick = onPlayButtonClick,
            )
        }
    }
}
