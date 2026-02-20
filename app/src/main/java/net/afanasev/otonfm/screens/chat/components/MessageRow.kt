package net.afanasev.otonfm.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.afanasev.otonfm.data.chat.MessageModel
import net.afanasev.otonfm.data.chat.MessageType
import net.afanasev.otonfm.ui.components.FlagIcon

private val AdminNameColor = Color(0xFFCD0200)

@Composable
fun MessageRow(
    message: MessageModel,
    modifier: Modifier = Modifier,
) {
    when (MessageType.fromValue(message.type)) {
        MessageType.USER_MESSAGE -> UserMessageRow(message, modifier)
        MessageType.SONG_REQUEST -> SongRequestRow(message, modifier)
        MessageType.ADMIN_ANNOUNCEMENT -> AdminAnnouncementRow(message, modifier)
        MessageType.SYSTEM -> SystemMessageRow(message, modifier)
    }
}

@Composable
private fun UserMessageRow(message: MessageModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
        verticalAlignment = Alignment.Top,
    ) {
        FlagIcon(message.authorFlag, 18.dp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = if (message.isAuthorAdmin) AdminNameColor else Color.Unspecified,
                    )
                ) {
                    append(message.authorName)
                }
                append("  ")
                append(message.text)
            },
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SongRequestRow(message: MessageModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FlagIcon(message.authorFlag, 18.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = message.authorName,
                fontWeight = FontWeight.Bold,
                color = if (message.isAuthorAdmin) AdminNameColor else Color.Unspecified,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Filled.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "${message.songArtist} — ${message.songTitle}",
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        if (message.text.isNotBlank()) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 20.dp),
            )
        }
    }
}

@Composable
private fun AdminAnnouncementRow(message: MessageModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .background(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (message.isPinned) {
            Icon(
                imageVector = Icons.Filled.PushPin,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = message.text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SystemMessageRow(message: MessageModel, modifier: Modifier = Modifier) {
    Text(
        text = message.text,
        style = MaterialTheme.typography.bodySmall,
        color = Color.Gray,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}
