package net.afanasev.otonfm.screens.auth

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.afanasev.otonfm.R
import net.afanasev.otonfm.data.auth.AuthRepository
import net.afanasev.otonfm.data.auth.UserModel
import net.afanasev.otonfm.data.auth.UserRepository
import net.afanasev.otonfm.log.Logger

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _navigateToRegistration = MutableSharedFlow<Unit>()
    val navigateToRegistration: SharedFlow<Unit> = _navigateToRegistration.asSharedFlow()

    private var pendingSignIn = false

    init {
        authRepository.observeAuthState()
            .onEach { firebaseUser ->
                if (firebaseUser == null) {
                    _authState.value = AuthState.NotAuthenticated
                } else {
                    checkUserProfile(firebaseUser.uid)
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun checkUserProfile(uid: String) {
        try {
            if (userRepository.userExists(uid)) {
                observeUserProfile(uid)
            } else {
                _authState.value = AuthState.NeedsRegistration
                if (pendingSignIn) {
                    pendingSignIn = false
                    _navigateToRegistration.emit(Unit)
                }
            }
        } catch (e: Exception) {
            Logger.logChatError("Check profile error: ${e.message}")
            _authState.value = AuthState.NotAuthenticated
            pendingSignIn = false
        }
    }

    private fun observeUserProfile(uid: String) {
        userRepository.observeUser(uid)
            .onEach { user ->
                if (user != null) {
                    _authState.value = AuthState.Authenticated(uid, user)
                }
            }
            .launchIn(viewModelScope)
    }

    fun signIn(context: Context) {
        viewModelScope.launch {
            pendingSignIn = true
            _authState.value = AuthState.Loading
            Logger.onChatSignIn()
            val result = authRepository.signInWithGoogle(context)
            result.onFailure { e ->
                Logger.logChatError("Sign-in failed: ${e.message}")
                _authState.value = AuthState.NotAuthenticated
                pendingSignIn = false
                Toast.makeText(context, R.string.chat_sign_in_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun requestRegistration() {
        viewModelScope.launch {
            _navigateToRegistration.emit(Unit)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun register(displayName: String, countryFlag: String) {
        val uid = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                userRepository.createUser(uid, displayName, countryFlag)
                observeUserProfile(uid)
            } catch (e: Exception) {
                Logger.logChatError("Registration error: ${e.message}")
            }
        }
    }

    sealed interface AuthState {
        data object Loading : AuthState
        data object NotAuthenticated : AuthState
        data object NeedsRegistration : AuthState
        data class Authenticated(val uid: String, val user: UserModel) : AuthState
    }
}
