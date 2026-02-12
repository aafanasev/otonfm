package net.afanasev.otonfm.screens.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import net.afanasev.otonfm.MainRoutes
import net.afanasev.otonfm.R
import net.afanasev.otonfm.log.Logger
import net.afanasev.otonfm.ui.components.IconTextRowItem

@Composable
fun MenuScreen(onItemSelected: (NavKey) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        IconTextRowItem(
            icon = Icons.Outlined.Palette,
            stringResId = R.string.menu_theme,
            onClick = {
                Logger.onMenuThemeClick()
                onItemSelected(MainRoutes.ThemeChooser)
            },
        )
        IconTextRowItem(
            icon = Icons.Outlined.AlternateEmail,
            stringResId = R.string.menu_contacts,
            onClick = {
                Logger.onMenuContactsClick()
                onItemSelected(MainRoutes.Contacts)
            },
        )
    }
}
