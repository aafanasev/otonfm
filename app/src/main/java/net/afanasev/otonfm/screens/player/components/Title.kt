package net.afanasev.otonfm.screens.player.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Title(
    text: String,
    modifier: Modifier,
) {
    SelectionContainer {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
fun TitlePreview() {
    Title(
        text = "Michael Jackson - Billie Jean",
        modifier = Modifier.width(100.dp),
    )
}
