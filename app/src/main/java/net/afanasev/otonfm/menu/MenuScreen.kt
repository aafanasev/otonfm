package net.afanasev.otonfm.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.common.log.Logger
import net.afanasev.otonfm.common.ui.components.IconTextRowItem

@Composable
fun MenuScreen(
    isSignedIn: Boolean,
    onThemeClick: () -> Unit,
    onContactsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSignOut: () -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        IconTextRowItem(
            icon = Icons.Outlined.Palette,
            stringResId = R.string.menu_theme,
            onClick = {
                Logger.onMenuThemeClick()
                onThemeClick()
            },
        )
        IconTextRowItem(
            icon = Icons.Outlined.AlternateEmail,
            stringResId = R.string.menu_contacts,
            onClick = {
                Logger.onMenuContactsClick()
                onContactsClick()
            },
        )
        if (isSignedIn) {
            IconTextRowItem(
                icon = Icons.Outlined.AccountCircle,
                stringResId = R.string.profile_setup_title,
                onClick = onProfileClick,
            )
            IconTextRowItem(
                icon = Icons.AutoMirrored.Outlined.Logout,
                stringResId = R.string.menu_sign_out,
                onClick = onSignOut,
            )
        }
    }
}
