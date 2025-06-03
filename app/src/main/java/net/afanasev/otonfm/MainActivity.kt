package net.afanasev.otonfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import net.afanasev.otonfm.player.PlayerViewScreen
import net.afanasev.otonfm.settings.SettingsViewScreen
import net.afanasev.otonfm.ui.theme.OtonfmTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OtonfmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "player") {
                        composable("player") {
                            PlayerViewScreen(
                                viewModel(),
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                navController.navigate("settings")
                            }
                        }
                        dialog("settings") {
                            SettingsViewScreen()
                        }
                    }
                }
            }
        }
    }
}
}
