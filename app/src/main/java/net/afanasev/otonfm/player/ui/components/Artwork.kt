package net.afanasev.otonfm.player.ui.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.ui.graphics.graphicsLayer
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

/** How far the artwork drifts above and below its place, and how long one way takes. */
private val FLOAT_HEIGHT = 6.dp
private const val FLOAT_PERIOD_MS = 4_000

/** How far the artwork tilts each way, and how long one way takes. */
private const val TILT_DEGREES = 1.5f
private const val TILT_PERIOD_MS = 5_500

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
    // Drifts slowly up and down and tilts a little, like a cloud. The two motions have different
    // periods, so together they never settle into an obvious loop. Read only in graphicsLayer, so
    // the drift redraws a layer without recomposing.
    val drift = rememberInfiniteTransition(label = "artworkDrift")
    val lift by drift.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(FLOAT_PERIOD_MS, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "lift",
    )
    val tilt by drift.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(TILT_PERIOD_MS, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "tilt",
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                Modifier
                    .aspectRatio(1f)
                    .graphicsLayer {
                        translationY = lift * FLOAT_HEIGHT.toPx()
                        rotationZ = tilt * TILT_DEGREES
                    }
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
