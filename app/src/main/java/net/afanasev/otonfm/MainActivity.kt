package net.afanasev.otonfm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
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
import net.afanasev.otonfm.screens.auth.AuthViewModel
import net.afanasev.otonfm.screens.chat.ChatScreen
import net.afanasev.otonfm.screens.chat.ChatViewModel
import net.afanasev.otonfm.screens.contacts.ContactsScreen
import net.afanasev.otonfm.screens.menu.MenuScreen
import net.afanasev.otonfm.screens.player.PlayerViewScreen
import net.afanasev.otonfm.screens.registration.ProfileSetupScreen
import net.afanasev.otonfm.screens.themechooser.ThemeChooserScreen
import net.afanasev.otonfm.ui.navigation.BottomSheetSceneStrategy
import net.afanasev.otonfm.ui.theme.OtonFmTheme
import net.afanasev.otonfm.ui.theme.Theme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
                    .launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

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
                    val authViewModel: AuthViewModel = viewModel()

                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        sceneStrategy = bottomSheetStrategy,
                        modifier = Modifier.padding(innerPadding),
                        entryProvider = entryProvider {
                            entry<MainRoutes.Player> {
                                PlayerViewScreen(
                                    viewModel(),
                                    onMenuClick = { backStack.add(MainRoutes.Menu) },
                                    onChatClick = { backStack.add(MainRoutes.Chat) },
                                    isDarkMode = isDarkMode,
                                    useArtworkAsBackground = theme == Theme.ARTWORK,
                                )
                            }
                            entry<MainRoutes.Menu>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                val authState by authViewModel.authState.collectAsState()
                                MenuScreen(
                                    isSignedIn = authState is AuthViewModel.AuthState.Authenticated,
                                    onItemSelected = { route ->
                                        backStack.removeLastOrNull()
                                        backStack.add(route)
                                    },
                                    onSignOut = {
                                        authViewModel.signOut()
                                        backStack.removeLastOrNull()
                                    },
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
                            entry<MainRoutes.ProfileSetup> {
                                ProfileSetupScreen(
                                    onRegister = { displayName, countryFlag ->
                                        authViewModel.register(displayName, countryFlag)
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                            entry<MainRoutes.Chat>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                val chatViewModel: ChatViewModel = viewModel()

                                LaunchedEffect(Unit) {
                                    authViewModel.navigateToRegistration.collect {
                                        backStack.add(MainRoutes.ProfileSetup)
                                    }
                                }

                                ChatScreen(chatViewModel, authViewModel)
                            }
                        }
                    )
                }
            }
        }
    }
}
