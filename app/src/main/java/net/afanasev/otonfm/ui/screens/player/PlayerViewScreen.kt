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
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.util.log.Logger
import net.afanasev.otonfm.ui.screens.player.components.AdminStatusBar
import net.afanasev.otonfm.ui.screens.player.components.Background
import net.afanasev.otonfm.ui.screens.player.components.ChatButton
import net.afanasev.otonfm.ui.screens.player.components.LandscapeContent
import net.afanasev.otonfm.ui.screens.player.components.MenuButton
import net.afanasev.otonfm.ui.screens.player.components.PortraitContent

@Composable
fun PlayerViewScreen(
    viewModel: PlayerViewModel,
    onMenuClick: () -> Unit,
    onChatClick: () -> Unit,
    isDarkMode: Boolean,
    useArtworkAsBackground: Boolean,
) {
    val adminStatus by viewModel.adminStatus.collectAsState()
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

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(48.dp))

                val status = adminStatus
                if (status != null && status.isActive) {
                    AdminStatusBar(
                        adminStatus = status,
                        isDarkMode = isDarkMode,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                MenuButton(
                    onNavigate = onMenuClick,
                    isDarkMode = isDarkMode,
                    modifier = Modifier,
                )
            }

            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LandscapeContent(
                    title = title,
                    nextTrackTitle = nextTrackTitle,
                    buttonState = buttonState,
                    artworkUri = artwork,
                    isDarkMode = isDarkMode,
                    onPlayClick = onPlayButtonClick,
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
                    isDarkMode = isDarkMode,
                    onPlayClick = onPlayButtonClick,
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
