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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import net.afanasev.otonfm.auth.data.DataStoreManager
import net.afanasev.otonfm.auth.ui.AuthViewModel
import net.afanasev.otonfm.auth.ui.ProfileSetupScreen
import net.afanasev.otonfm.chat.ui.ChatScreen
import net.afanasev.otonfm.chat.ui.ChatViewModel
import net.afanasev.otonfm.common.navigation.BottomSheetSceneStrategy
import net.afanasev.otonfm.common.ui.style.OtonFmTheme
import net.afanasev.otonfm.contacts.ContactsScreen
import net.afanasev.otonfm.menu.MenuScreen
import net.afanasev.otonfm.theme.data.ThemeMode
import net.afanasev.otonfm.theme.data.ThemeStore
import net.afanasev.otonfm.theme.ui.ThemeChooserScreen

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNotifications()
        val dataStore = DataStoreManager(applicationContext)
        val themeStore = ThemeStore(applicationContext)

        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val theme by themeStore.theme.collectAsStateWithLifecycle(ThemeMode.SYSTEM)
            val isDarkMode = when (theme) {
                ThemeMode.ARTWORK -> true
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            OtonFmTheme(isDarkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backStack = rememberNavBackStack(MainRoutes.Player)
                    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
                    val authViewModel: AuthViewModel = viewModel()

                    NavDisplay(
                        backStack = backStack,
                        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                        sceneStrategies = listOf(bottomSheetStrategy),
                        modifier = Modifier.padding(innerPadding),
                        entryProvider = entryProvider {
                            entry<MainRoutes.Player> {
                                MainScreen(
                                    playerViewModel = viewModel(),
                                    adminStatusViewModel = viewModel(),
                                    isDarkMode = isDarkMode,
                                    useArtworkAsBackground = theme == ThemeMode.ARTWORK,
                                    onMenuClick = { backStack.add(MainRoutes.Menu) },
                                    onChatClick = { backStack.add(MainRoutes.Chat) },
                                )
                            }
                            entry<MainRoutes.Menu>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                val authState by authViewModel.authState.collectAsStateWithLifecycle()
                                fun replaceMenuWith(route: NavKey) {
                                    backStack.removeLastOrNull()
                                    backStack.add(route)
                                }

                                MenuScreen(
                                    isSignedIn = authState is AuthViewModel.AuthState.Authenticated,
                                    onThemeClick = { replaceMenuWith(MainRoutes.ThemeChooser) },
                                    onContactsClick = { replaceMenuWith(MainRoutes.Contacts) },
                                    onProfileClick = { replaceMenuWith(MainRoutes.ProfileSetup) },
                                    onSignOut = {
                                        authViewModel.signOut()
                                        scope.launch { dataStore.saveLastProfileUpdateAt(0L) }
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                            entry<MainRoutes.ThemeChooser>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                ThemeChooserScreen(onThemeSelected = {
                                    scope.launch { themeStore.saveTheme(it) }
                                    backStack.removeLastOrNull()
                                })
                            }
                            entry<MainRoutes.Contacts>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                ContactsScreen()
                            }
                            entry<MainRoutes.ProfileSetup> {
                                val authState by authViewModel.authState.collectAsStateWithLifecycle()
                                val currentUser = (authState as? AuthViewModel.AuthState.Authenticated)?.user
                                val isEditMode = authState is AuthViewModel.AuthState.Authenticated
                                val lastUpdate by dataStore.lastProfileUpdateAt.collectAsStateWithLifecycle(0L)
                                val canEditProfile = !isEditMode || System.currentTimeMillis() - lastUpdate > 3_600_000L

                                ProfileSetupScreen(
                                    initialName = currentUser?.displayName ?: "",
                                    initialFlag = currentUser?.countryFlag?.takeIf { it.isNotEmpty() },
                                    submitEnabled = canEditProfile,
                                    onProfileSetup = { displayName, countryFlag ->
                                        if (isEditMode) {
                                            authViewModel.updateProfile(displayName, countryFlag)
                                            scope.launch { dataStore.saveLastProfileUpdateAt(System.currentTimeMillis()) }
                                        } else {
                                            authViewModel.saveProfile(displayName, countryFlag)
                                        }
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                            entry<MainRoutes.Chat>(
                                metadata = BottomSheetSceneStrategy.bottomSheet()
                            ) {
                                val chatViewModel: ChatViewModel = viewModel()
                                val context = LocalContext.current

                                LaunchedEffect(Unit) {
                                    authViewModel.navigateToProfileSetup.collect {
                                        backStack.add(MainRoutes.ProfileSetup)
                                    }
                                }

                                ChatScreen(
                                    chatViewModel = chatViewModel,
                                    onSend = {
                                        when (val state = authViewModel.authState.value) {
                                            is AuthViewModel.AuthState.Authenticated ->
                                                chatViewModel.sendMessage(state.uid, state.user)

                                            is AuthViewModel.AuthState.NotAuthenticated ->
                                                authViewModel.signIn(context)

                                            is AuthViewModel.AuthState.NeedsProfileSetup ->
                                                authViewModel.requestProfileSetup()

                                            is AuthViewModel.AuthState.Loading -> {}
                                        }
                                    },
                                )
                            }
                        })
                }
            }
        }
    }

    private fun setupNotifications() {
        fun subscribeToTopic() {
            FirebaseMessaging.getInstance().subscribeToTopic("all_users")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                subscribeToTopic()
            } else {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                    if (granted) {
                        subscribeToTopic()
                    }
                }.launch(permission)
            }
        } else {
            subscribeToTopic()
        }
    }
}
