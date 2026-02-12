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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.log.Logger
import net.afanasev.otonfm.screens.player.components.AdminStatusBar
import net.afanasev.otonfm.screens.player.components.Artwork
import net.afanasev.otonfm.screens.player.components.Background
import net.afanasev.otonfm.screens.player.components.Logo
import net.afanasev.otonfm.screens.player.components.MenuButton
import net.afanasev.otonfm.screens.player.components.NextTrack
import net.afanasev.otonfm.screens.player.components.PlayButton
import net.afanasev.otonfm.screens.player.components.ChatButton
import net.afanasev.otonfm.screens.player.components.Title

@Composable
fun PlayerViewScreen(
    viewModel: PlayerViewModel,
    onMenuClick: () -> Unit,
    onChatClick: () -> Unit,
    latestChatMessage: String?,
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

            ChatButton(
                latestMessage = latestChatMessage,
                isDarkMode = isDarkMode,
                onClick = {
                    Logger.onChatButtonClick()
                    onChatClick()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
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
        }
    }
}
