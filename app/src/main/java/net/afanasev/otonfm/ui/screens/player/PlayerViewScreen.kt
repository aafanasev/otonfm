package net.afanasev.otonfm.ui.screens.player

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.screens.player.components.ChatButton
import net.afanasev.otonfm.ui.screens.player.components.Logo
import net.afanasev.otonfm.ui.screens.player.components.MenuButton
import net.afanasev.otonfm.ui.theme.BACKGROUND_GRADIENTS
import net.afanasev.otonfm.util.log.Logger
import net.afanasev.radioplayer.core.player.PlayerViewModel
import net.afanasev.radioplayer.core.player.ui.Background
import net.afanasev.radioplayer.core.player.ui.LandscapeContent
import net.afanasev.radioplayer.core.player.ui.PortraitContent

@Composable
fun PlayerViewScreen(
    viewModel: PlayerViewModel,
    onMenuClick: () -> Unit,
    onChatClick: () -> Unit,
    isDarkMode: Boolean,
    useArtworkAsBackground: Boolean,
) {
    val artwork by viewModel.artworkUri.collectAsState()
    val title by viewModel.title.collectAsState()
    val nextTrackTitle by viewModel.nextTrackTitle.collectAsState()
    val buttonState by viewModel.buttonState.collectAsState()
    val configuration = LocalConfiguration.current
    val nextTrackPrefix = stringResource(R.string.player_next_track_prefix)

    Box(modifier = Modifier.fillMaxSize()) {
        if (useArtworkAsBackground) {
            Background(
                artworkUri = artwork,
                defaultArtworkUri = stringResource(R.string.default_artwork_uri),
                gradients = BACKGROUND_GRADIENTS.map { it[0] to it[1] },
                modifier = Modifier.fillMaxSize(),
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.weight(1f))

                MenuButton(
                    onNavigate = onMenuClick,
                    isDarkMode = isDarkMode,
                    modifier = Modifier,
                )
            }

            val logo: @Composable () -> Unit = {
                Logo(
                    isDarkMode = isDarkMode,
                    modifier = Modifier.fillMaxWidth(0.6f),
                )
            }

            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LandscapeContent(
                    title = title,
                    nextTrackTitle = nextTrackTitle,
                    buttonState = buttonState,
                    artworkUri = artwork,
                    onPlayClick = viewModel::playPause,
                    logo = logo,
                    nextTrackPrefix = nextTrackPrefix,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                )
            } else {
                PortraitContent(
                    title = title,
                    nextTrackTitle = nextTrackTitle,
                    buttonState = buttonState,
                    artworkUri = artwork,
                    onPlayClick = viewModel::playPause,
                    logo = logo,
                    nextTrackPrefix = nextTrackPrefix,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            }

            ChatButton(
                isDarkMode = isDarkMode,
                onClick = {
                    Logger.onChatButtonClick()
                    onChatClick()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
            )
        }
    }
}
