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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.afanasev.otonfm.log.Logger
import net.afanasev.otonfm.screens.player.components.Artwork
import net.afanasev.otonfm.screens.player.components.Background
import net.afanasev.otonfm.screens.player.components.Logo
import net.afanasev.otonfm.screens.player.components.Menu
import net.afanasev.otonfm.screens.player.components.NextTrack
import net.afanasev.otonfm.screens.player.components.PlayButton
import net.afanasev.otonfm.screens.player.components.Title

@Composable
fun PlayerViewScreen(
    viewModel: PlayerViewModel,
    navController: NavController,
    isDarkMode: Boolean,
    useArtworkAsBackground: Boolean,
) {
    val artwork by viewModel.artworkUri.collectAsState()
    val title by viewModel.title.collectAsState()
    val nextTrackTitle by viewModel.nextTrackTitle.collectAsState()
    val buttonState by viewModel.buttonState.collectAsState()

    val configuration = LocalConfiguration.current
    val onPlayButtonClick = {
        Logger.onPlayButtonClick(buttonState)
        viewModel.playPause()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (useArtworkAsBackground) {
            Background(
                artwork,
                modifier = Modifier.fillMaxSize(),
            )
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
                        artworkUri = artwork,
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
                    Spacer(modifier = Modifier.height(8.dp))
                    NextTrack(
                        text = nextTrackTitle,
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
                    artworkUri = artwork,
                    modifier = Modifier.fillMaxWidth(0.8f),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Title(
                    text = title,
                    modifier = Modifier.fillMaxWidth(0.8f),
                )
                Spacer(modifier = Modifier.height(8.dp))
                NextTrack(
                    text = nextTrackTitle,
                    modifier = Modifier.fillMaxWidth(0.8f),
                )
                Spacer(modifier = Modifier.height(36.dp))
                PlayButton(
                    buttonState = buttonState,
                    onClick = onPlayButtonClick,
                )
            }
        }

        Menu(
            navController,
            isDarkMode,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp),
        )
    }
}
