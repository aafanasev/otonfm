package net.afanasev.otonfm.player.ui.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import net.afanasev.otonfm.common.ui.components.MarqueeText

/**
 * A quiet line under the title: a small uppercase [prefix] label, then the next track. [prefix] is
 * plain text (not an Android string resource) so callers can localize it however they like.
 */
@Composable
fun NextTrack(
    text: String,
    modifier: Modifier = Modifier,
    prefix: String = "Next",
) {
    val labelStyle = MaterialTheme.typography.labelSmall
        .copy(fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp)
        .toSpanStyle()
    val line = if (text.isEmpty()) {
        AnnotatedString("")
    } else {
        buildAnnotatedString {
            // The prefix is translated, so uppercase it by the app's language rules.
            withStyle(labelStyle) { append(prefix.toUpperCase(Locale.current)) }
            append("  ")
            append(text)
        }
    }
    MarqueeText(
        text = line,
        style = MaterialTheme.typography.bodyMedium,
        color = LocalContentColor.current.copy(alpha = 0.65f),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun NextTrackPreview() {
    NextTrack(text = "OTON FM - Ырыа-тойук")
}
