package net.afanasev.otonfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import net.afanasev.otonfm.data.prefs.DataStoreManager
import net.afanasev.otonfm.screens.player.PlayerViewScreen
import net.afanasev.otonfm.screens.themechooser.ThemeChooserScreen
import net.afanasev.otonfm.ui.theme.OtonFmTheme
import net.afanasev.otonfm.ui.theme.Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = DataStoreManager(applicationContext)

        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val theme by dataStore.theme.collectAsState("system")
            val isDarkMode = when (theme) {
                Theme.DARK -> true
                Theme.LIGHT -> false
                else -> isSystemInDarkTheme()
            }

            OtonFmTheme(isDarkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController,
                        startDestination = MainRoutes.Player,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<MainRoutes.Player> {
                            PlayerViewScreen(
                                viewModel(),
                                isDarkMode = isDarkMode,
                                onArtworkLongClick = {
                                    navController.navigate(MainRoutes.ThemeChooser)
                                }
                            )
                        }
                        dialog<MainRoutes.ThemeChooser> {
                            ThemeChooserScreen(onThemeSelected = {
                                scope.launch { dataStore.saveTheme(it) }
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}
