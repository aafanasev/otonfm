package net.afanasev.otonfm.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import net.afanasev.otonfm.screens.auth.AuthViewModel
import net.afanasev.otonfm.screens.auth.AuthViewModel.AuthState
import net.afanasev.otonfm.screens.chat.components.ChatInputBar
import net.afanasev.otonfm.screens.chat.components.MessageRow
import net.afanasev.otonfm.screens.chat.components.PinnedMessageBanner

@Composable
fun ChatScreen(chatViewModel: ChatViewModel, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val authState by authViewModel.authState.collectAsState()
    val messages by chatViewModel.messages.collectAsState()
    val inputText by chatViewModel.inputText.collectAsState()
    val canSend by chatViewModel.canSend.collectAsState()
    val containsProfanity by chatViewModel.containsProfanity.collectAsState()
    val pinnedMessage = messages.lastOrNull { it.isPinned }

    LaunchedEffect(messages.size) {
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
            onSend = {
                when (val state = authState) {
                    is AuthState.Authenticated ->
                        chatViewModel.sendMessage(state.uid, state.user)

                    is AuthState.NotAuthenticated ->
                        authViewModel.signIn(context)

                    is AuthState.NeedsProfileSetup ->
                        authViewModel.requestProfileSetup()

                    is AuthState.Loading -> {}
                }
            },
        )
    }
}
