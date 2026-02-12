package net.afanasev.otonfm.screens.player.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import net.afanasev.otonfm.log.Logger

@Composable
fun MenuButton(
    onNavigate: () -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier,
) {
    IconButton(
        onClick = {
            Logger.onMenuClick()
            onNavigate()
        },
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Menu",
            tint = if (isDarkMode) Color.White else Color.Black,
        )
    }
}
