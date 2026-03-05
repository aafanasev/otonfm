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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.data.chat.MessageModel
import net.afanasev.otonfm.data.chat.MessageType
import net.afanasev.otonfm.ui.components.FlagIcon
import net.afanasev.otonfm.ui.theme.UserNameColors
import kotlin.math.abs

private val AdminNameColor = Color(0xFFCD0200)

private fun colorForName(name: String): Color =
    UserNameColors[abs(name.hashCode()) % UserNameColors.size]

@Composable
private fun AuthorHeader(
    authorFlag: String,
    authorName: String,
    authorIsAdmin: Boolean,
    flagSize: Dp = 18.dp,
) {
    FlagIcon(authorFlag, flagSize)
    Spacer(modifier = Modifier.width(4.dp))
    Text(
        text = "$authorName:",
        fontWeight = FontWeight.Bold,
        color = if (authorIsAdmin) AdminNameColor else colorForName(authorName),
        style = MaterialTheme.typography.bodyMedium,
    )
}

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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuthorHeader(message.authorFlag, message.authorName, message.authorIsAdmin)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = message.text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
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
            AuthorHeader(message.authorFlag, message.authorName, message.authorIsAdmin)
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
