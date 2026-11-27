package net.afanasev.otonfm.player.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import net.afanasev.otonfm.BuildConfig
import net.afanasev.otonfm.R

/**
 * Shown while there's no artwork. Built in code rather than in strings.xml, because Coil loads
 * only the resource id form of android.resource URIs, not /drawable/<name>.
 */
val DEFAULT_ARTWORK_URI =
    "android.resource://${BuildConfig.APPLICATION_ID}/${R.drawable.default_artwork}"

/** How long things fade when switching between the default and real artwork. */
internal const val DEFAULT_ARTWORK_FADE_MS = 500

@Composable
fun Artwork(
    artworkUri: String,
    modifier: Modifier = Modifier,
) {
    val previewShape = RoundedCornerShape(12.dp)
    // The default artwork is translucent, so the box's shadow and background would show through
    // it. Both fade out while it shows, and come back for real artwork.
    val frameAlpha by animateFloatAsState(
        targetValue = if (artworkUri == DEFAULT_ARTWORK_URI) 0f else 1f,
        animationSpec = tween(DEFAULT_ARTWORK_FADE_MS),
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                Modifier
                    .aspectRatio(1f)
                    .shadow(
                        elevation = 6.dp * frameAlpha,
                        shape = previewShape,
                        ambientColor = Color.DarkGray,
                        spotColor = Color.DarkGray,
                    ),
            )
            .clip(previewShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = frameAlpha)),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(artworkUri)
                .crossfade(true)
                .build(),
            contentDescription = "Preview",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun ArtworkNoImagePreview() {
    Artwork(
        artworkUri = "",
        modifier = Modifier.width(60.dp),
    )
}
