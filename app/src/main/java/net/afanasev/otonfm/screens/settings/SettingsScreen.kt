package net.afanasev.otonfm.screens.settings

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import net.afanasev.otonfm.MainRoutes
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.components.Dialog
import net.afanasev.otonfm.ui.components.DialogItem

@Composable
fun SettingsScreen(navController: NavHostController) {
    Dialog {
        DialogItem(
            R.string.settings_theme,
            onClick = {
                navController.navigate(MainRoutes.ThemeChooser) {
                    popUpTo<MainRoutes.Settings> {
                        inclusive = true
                    }
                }
            },
        )

        HorizontalDivider()

        DialogItem(
            R.string.settings_contacts,
            onClick = {
                navController.navigate(MainRoutes.Contacts) {
                    popUpTo<MainRoutes.Settings> {
                        inclusive = true
                    }
                }
            },
        )
    }
}