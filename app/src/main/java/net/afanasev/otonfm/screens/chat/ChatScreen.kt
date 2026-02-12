package net.afanasev.otonfm.screens.chat

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.screens.chat.components.ChatContent

@Composable
fun ChatScreen(chatViewModel: ChatViewModel) {
    val messages by chatViewModel.messages.collectAsState()
    val inputText by chatViewModel.inputText.collectAsState()

    Text(
        text = stringResource(R.string.chat_title),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )

    ChatContent(
        messages = messages,
        inputText = inputText,
        onTextChange = chatViewModel::updateInputText,
        onSend = chatViewModel::sendMessage,
        modifier = Modifier.fillMaxHeight(0.6f),
    )
}
