package net.afanasev.otonfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import net.afanasev.otonfm.screens.player.PlayerViewScreen
import net.afanasev.otonfm.screens.themechooser.ThemeChooserScreen
import net.afanasev.otonfm.ui.theme.OtonfmTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf("dark") }

            OtonfmTheme(
                darkTheme = when (darkTheme) {
                    "light" -> false
                    "dark" -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
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
                                onPlayButtonLongClick = {
                                    navController.navigate(MainRoutes.ThemeChooser)
                                }
                            )
                        }
                        dialog<MainRoutes.ThemeChooser> {
                            ThemeChooserScreen(onThemeSelected = {
                                darkTheme = it
                            })
                        }
                    }
                }
            }
        }
    }
}
