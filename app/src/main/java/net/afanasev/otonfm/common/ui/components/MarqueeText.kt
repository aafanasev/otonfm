package net.afanasev.otonfm.common.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

/** One line of text that scrolls when it doesn't fit, and is centered when it does. */
@Composable
fun MarqueeText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
) {
    // A new marquee for each text, so a scroll position left over from a longer text can't push
    // a shorter one off center.
    key(text) {
        Text(
            text = text,
            color = color,
            style = style,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = modifier.basicMarquee(iterations = Int.MAX_VALUE),
        )
    }
}

@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
) {
    MarqueeText(AnnotatedString(text), modifier, style, color)
}
