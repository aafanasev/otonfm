package net.afanasev.otonfm.ui.screens.chat

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.afanasev.otonfm.R
import net.afanasev.otonfm.data.auth.UserModel
import net.afanasev.otonfm.data.chat.ChatRepository
import net.afanasev.otonfm.data.chat.MessageModel
import net.afanasev.otonfm.util.Logger
import net.afanasev.otonfm.util.profanity.ProfanityFilter

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository()

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _canSend = MutableStateFlow(true)
    val canSend: StateFlow<Boolean> = _canSend.asStateFlow()

    private val _containsProfanity = MutableStateFlow(false)
    val containsProfanity: StateFlow<Boolean> = _containsProfanity.asStateFlow()

    init {
        chatRepository.observeMessages()
            .onEach { _messages.value = it }
            .launchIn(viewModelScope)
    }

    fun sendMessage(uid: String, user: UserModel) {
        val text = _inputText.value.trim()
        if (text.isEmpty() || text.length > 500) return
        if (ProfanityFilter.containsProfanity(text)) return
        if (!_canSend.value) return
        _inputText.value = ""
        _canSend.value = false
        Logger.onChatMessageSend()
        viewModelScope.launch {
            try {
                chatRepository.sendMessage(text, uid, user)
                delay(10_000)
                _canSend.value = true
            } catch (e: Exception) {
                Logger.logChatError("Send message error: ${e.message}")
                _inputText.value = text
                _canSend.value = true
                Toast.makeText(
                    getApplication(),
                    R.string.chat_send_error,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    fun updateInputText(text: String) {
        val trimmed = text.take(500)
        _inputText.value = trimmed
        _containsProfanity.value = ProfanityFilter.containsProfanity(trimmed)
    }
}
