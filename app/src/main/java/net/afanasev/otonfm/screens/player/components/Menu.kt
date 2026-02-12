package net.afanasev.otonfm.screens.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import net.afanasev.otonfm.MainRoutes
import net.afanasev.otonfm.R
import net.afanasev.otonfm.log.Logger

@Composable
fun Menu(
    onNavigate: (NavKey) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = {
                Logger.onMenuClick()
                expanded = !expanded
            },
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Menu",
                tint = if (isDarkMode) Color.White else Color.Black,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_theme)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Palette,
                        contentDescription = null,
                        tint = if (isDarkMode) Color.White else Color.Black
                    )
                },
                onClick = {
                    Logger.onMenuThemeClick()
                    expanded = false
                    onNavigate(MainRoutes.ThemeChooser)
                })
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_contacts)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AlternateEmail,
                        contentDescription = null,
                        tint = if (isDarkMode) Color.White else Color.Black
                    )
                },
                onClick = {
                    Logger.onMenuContactsClick()
                    expanded = false
                    onNavigate(MainRoutes.Contacts)
                })
        }
    }
}