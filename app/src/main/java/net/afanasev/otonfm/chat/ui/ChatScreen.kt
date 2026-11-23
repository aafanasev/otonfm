package net.afanasev.otonfm.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.afanasev.otonfm.chat.ui.components.ChatInputBar
import net.afanasev.otonfm.chat.ui.components.MessageRow
import net.afanasev.otonfm.chat.ui.components.PinnedMessageBanner

@Composable
fun ChatScreen(chatViewModel: ChatViewModel, onSend: () -> Unit) {
    val listState = rememberLazyListState()

    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val inputText by chatViewModel.inputText.collectAsStateWithLifecycle()
    val canSend by chatViewModel.canSend.collectAsStateWithLifecycle()
    val containsProfanity by chatViewModel.containsProfanity.collectAsStateWithLifecycle()
    val pinnedMessage by chatViewModel.pinnedMessage.collectAsStateWithLifecycle()

    LaunchedEffect(messages.lastOrNull()?.id) {
        if (messages.isNotEmpty() && listState.firstVisibleItemIndex == 0) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.75f)
            .imePadding()
    ) {
        pinnedMessage?.let { PinnedMessageBanner(message = it) }
        LazyColumn(
            state = listState,
            reverseLayout = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(messages.asReversed(), key = { it.id }) { message ->
                MessageRow(message = message)
            }
        }

        ChatInputBar(
            text = inputText,
            containsProfanity = containsProfanity,
            canSend = canSend,
            onTextChange = chatViewModel::updateInputText,
            onSend = onSend,
        )
    }
}
