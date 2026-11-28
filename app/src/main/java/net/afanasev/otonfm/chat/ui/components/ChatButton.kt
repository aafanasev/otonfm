package net.afanasev.otonfm.chat.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R
import net.afanasev.otonfm.common.ui.components.Pill
import net.afanasev.otonfm.common.ui.style.OtonFmTheme

@Composable
fun ChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Pill(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = stringResource(R.string.chat_button_label))
    }
}

@Preview
@Composable
private fun ChatButtonPreview() {
    OtonFmTheme(isDarkMode = false) {
        ChatButton(onClick = {})
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun ChatButtonDarkPreview() {
    OtonFmTheme(isDarkMode = true) {
        ChatButton(onClick = {})
    }
}
