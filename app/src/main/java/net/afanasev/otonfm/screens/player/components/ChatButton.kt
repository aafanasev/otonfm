package net.afanasev.otonfm.screens.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.afanasev.otonfm.R

@Composable
fun ChatButton(
    latestMessage: String?,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (isDarkMode) Color.White else Color.Black
    val backgroundColor =
        if (isDarkMode) Color.Black.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.6f)

    Row(
        modifier = modifier
            .widthIn(max = 300.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(14.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = latestMessage ?: stringResource(R.string.chat_button_label),
            color = contentColor,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = if (latestMessage != null) Modifier.basicMarquee(iterations = Int.MAX_VALUE) else Modifier,
        )
    }
}

@Preview
@Composable
private fun ChatButtonDefaultPreview() {
    ChatButton(latestMessage = null, isDarkMode = false, onClick = {})
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun ChatButtonDarkPreview() {
    ChatButton(latestMessage = null, isDarkMode = true, onClick = {})
}

@Preview
@Composable
private fun ChatButtonWithMessagePreview() {
    ChatButton(
        latestMessage = "\uD83C\uDDF7\uD83C\uDDFA Hello everyone!",
        isDarkMode = false,
        onClick = {},
    )
}
