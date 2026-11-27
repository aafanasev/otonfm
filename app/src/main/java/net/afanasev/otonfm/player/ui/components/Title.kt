package net.afanasev.otonfm.player.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.common.ui.components.MarqueeText

/** A stream title split for display. [artist] is empty when the title isn't "Artist - Song". */
data class TrackTitle(val song: String, val artist: String = "")

private val ARTIST_SEPARATOR = Regex("""\s[-–—]\s""")

/**
 * Splits "Artist - Song" at the first dash with spaces around it, so dashes inside a song name
 * (like "Ырыа-тойук") stay. Anything else is all song.
 */
fun String.toTrackTitle(): TrackTitle {
    val separator = ARTIST_SEPARATOR.find(this) ?: return TrackTitle(song = trim())
    val artist = substring(0, separator.range.first).trim()
    val song = substring(separator.range.last + 1).trim()
    if (artist.isEmpty() || song.isEmpty()) return TrackTitle(song = trim())
    return TrackTitle(song = song, artist = artist)
}

/**
 * The song on top, the artist below in a quieter style. Each takes one line, scrolling when too
 * long, and the artist line keeps its space when empty, so the block's height never changes.
 */
@Composable
fun Title(
    title: TrackTitle,
    modifier: Modifier = Modifier,
) {
    SelectionContainer(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MarqueeText(
                text = title.song,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.fillMaxWidth(),
            )
            MarqueeText(
                text = title.artist,
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun TitlePreview() {
    Title(
        title = "Дайаана - Үрдүккэ көттөрбүн".toTrackTitle(),
        modifier = Modifier.width(240.dp),
    )
}
