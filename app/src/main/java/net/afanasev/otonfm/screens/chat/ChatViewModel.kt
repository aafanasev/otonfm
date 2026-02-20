package net.afanasev.otonfm.screens.chat

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import net.afanasev.otonfm.log.Logger

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository()

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    init {
        chatRepository.observeMessages()
            .onEach { _messages.value = it }
            .launchIn(viewModelScope)
    }

    fun sendMessage(uid: String, user: UserModel) {
        val text = _inputText.value.trim()
        if (text.isEmpty() || text.length > 500) return
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
}
