package net.afanasev.otonfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import net.afanasev.otonfm.data.prefs.DataStoreManager
import net.afanasev.otonfm.navigation.BottomSheetSceneStrategy
import net.afanasev.otonfm.screens.contacts.ContactsScreen
import net.afanasev.otonfm.screens.player.PlayerViewScreen
import net.afanasev.otonfm.screens.themechooser.ThemeChooserScreen
import net.afanasev.otonfm.ui.theme.OtonFmTheme
import net.afanasev.otonfm.ui.theme.Theme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = DataStoreManager(applicationContext)

        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val theme by dataStore.theme.collectAsState("system")
            val isDarkMode = when (theme) {
                Theme.ARTWORK -> true
                Theme.DARK -> true
                Theme.LIGHT -> false
                else -> isSystemInDarkTheme()
            }

            OtonFmTheme(isDarkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backStack = rememberNavBackStack(MainRoutes.Player)
                    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        sceneStrategy = bottomSheetStrategy,
                        modifier = Modifier.padding(innerPadding),
                        entryProvider = entryProvider {
                            entry<MainRoutes.Player> {
                                PlayerViewScreen(
                                    viewModel(),
                                    onNavigate = { backStack.add(it) },
                                    isDarkMode = isDarkMode,
                                    useArtworkAsBackground = theme == Theme.ARTWORK,
                                )
                            }
                            entry<MainRoutes.ThemeChooser>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                ThemeChooserScreen(onThemeSelected = {
                                    scope.launch { dataStore.saveTheme(it) }
                                    backStack.removeLastOrNull()
                                })
                            }
                            entry<MainRoutes.Contacts>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                ContactsScreen()
                            }
                        }
                    )
                }
            }
        }
    }
}
