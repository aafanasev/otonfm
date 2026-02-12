package net.afanasev.otonfm.screens.chat

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.afanasev.otonfm.R
import net.afanasev.otonfm.data.auth.AuthRepository
import net.afanasev.otonfm.data.auth.UserModel
import net.afanasev.otonfm.data.auth.UserRepository
import net.afanasev.otonfm.data.chat.ChatRepository
import net.afanasev.otonfm.data.chat.MessageModel
import net.afanasev.otonfm.log.Logger

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val chatRepository = ChatRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    val latestMessagePreview: StateFlow<String?> = _messages
        .map { msgs -> msgs.lastOrNull()?.let { "${it.authorFlag} ${it.text}" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _navigateAfterSignIn = MutableSharedFlow<AuthState>()
    val navigateAfterSignIn: SharedFlow<AuthState> = _navigateAfterSignIn.asSharedFlow()

    private var currentUser: UserModel? = null
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

        chatRepository.observeMessages()
            .onEach { _messages.value = it }
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
                    _navigateAfterSignIn.emit(AuthState.NeedsRegistration)
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
                    currentUser = user
                    val state = AuthState.Authenticated(user)
                    _authState.value = state
                    if (pendingSignIn) {
                        pendingSignIn = false
                        _navigateAfterSignIn.emit(state)
                    }
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
                Toast.makeText(context, R.string.chat_sign_in_error, Toast.LENGTH_SHORT).show()
            }
        }
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

    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isEmpty() || text.length > 500) return
        val uid = authRepository.currentUser?.uid ?: return
        val user = currentUser ?: return

        _inputText.value = ""
        Logger.onChatMessageSend()

        viewModelScope.launch {
            try {
                chatRepository.sendMessage(text, uid, user)
            } catch (e: Exception) {
                Logger.logChatError("Send message error: ${e.message}")
                _inputText.value = text
                Toast.makeText(
                    getApplication(),
                    R.string.chat_send_error,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    sealed interface AuthState {
        data object Loading : AuthState
        data object NotAuthenticated : AuthState
        data object NeedsRegistration : AuthState
        data class Authenticated(val user: UserModel) : AuthState
    }
}
