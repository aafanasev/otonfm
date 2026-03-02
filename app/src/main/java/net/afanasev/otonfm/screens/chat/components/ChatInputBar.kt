package net.afanasev.otonfm.screens.chat.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R

@Composable
fun ChatInputBar(
    text: String,
    containsProfanity: Boolean,
    canSend: Boolean,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isValid = text.isNotBlank() && !containsProfanity && canSend

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text(stringResource(R.string.chat_input_hint)) },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            isError = containsProfanity,
            supportingText = if (containsProfanity) {
                { Text(stringResource(R.string.chat_profanity_error)) }
            } else null,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.weight(1f),
        )
        IconButton(
            onClick = onSend,
            enabled = isValid,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.chat_send),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatInputBarEmptyPreview() {
    ChatInputBar(text = "", containsProfanity = false, canSend = true, onTextChange = {}, onSend = {})
}

@Preview(showBackground = true)
@Composable
private fun ChatInputBarWithTextPreview() {
    ChatInputBar(text = "Hello, world!", containsProfanity = false, canSend = true, onTextChange = {}, onSend = {})
}

@Preview(showBackground = true)
@Composable
private fun ChatInputBarProfanityErrorPreview() {
    ChatInputBar(text = "badword", containsProfanity = true, canSend = true, onTextChange = {}, onSend = {})
}
