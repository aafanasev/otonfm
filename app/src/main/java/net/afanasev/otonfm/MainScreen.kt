package net.afanasev.otonfm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.afanasev.otonfm.adminstatus.ui.AdminStatusBar
import net.afanasev.otonfm.adminstatus.ui.AdminStatusViewModel
import net.afanasev.otonfm.chat.ui.components.ChatButton
import net.afanasev.otonfm.common.log.Logger
import net.afanasev.otonfm.menu.MenuButton
import net.afanasev.otonfm.player.ui.PlayerBackground
import net.afanasev.otonfm.player.ui.PlayerContent
import net.afanasev.otonfm.player.ui.PlayerViewModel

/** The home screen: composes the independent features so they don't need to know each other. */
@Composable
fun MainScreen(
    playerViewModel: PlayerViewModel,
    adminStatusViewModel: AdminStatusViewModel,
    isDarkMode: Boolean,
    useArtworkAsBackground: Boolean,
    onMenuClick: () -> Unit,
    onChatClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (useArtworkAsBackground) {
            PlayerBackground(playerViewModel, modifier = Modifier.fillMaxSize())
        }

        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                adminStatusViewModel = adminStatusViewModel,
                isDarkMode = isDarkMode,
                onMenuClick = onMenuClick,
            )

            PlayerContent(
                viewModel = playerViewModel,
                isDarkMode = isDarkMode,
                modifier = Modifier.weight(1f),
            )

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

@Composable
private fun TopBar(
    adminStatusViewModel: AdminStatusViewModel,
    isDarkMode: Boolean,
    onMenuClick: () -> Unit,
) {
    val adminStatus by adminStatusViewModel.adminStatus.collectAsStateWithLifecycle()

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
}
