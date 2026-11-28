package net.afanasev.otonfm.player.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.delay
import net.afanasev.otonfm.common.ui.style.BACKGROUND_GRADIENTS
import kotlin.random.Random

private val Gradients: List<Pair<Color, Color>> = BACKGROUND_GRADIENTS.map { it[0] to it[1] }

/**
 * Darkens the lower part of the artwork background, where the title, next track and chat button
 * sit, so white text stays readable over light artwork. The artwork itself is at the top.
 */
private val ReadabilityScrim = Brush.verticalGradient(
    0f to Color.Transparent,
    0.45f to Color.Black.copy(alpha = 0.1f),
    1f to Color.Black.copy(alpha = 0.55f),
)

/**
 * Shows an animated gradient while [artworkUri] is null, then crossfades to a blurred artwork
 * background once real artwork is available.
 */
@Composable
fun Background(
    artworkUri: String?,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = artworkUri == null,
        animationSpec = tween(1_000),
    ) { showPlaceholder ->
        if (showPlaceholder) {
            GradientPlaceholder(modifier)
        } else {
            Box(modifier = modifier) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(artworkUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(
                        Color.Black.copy(alpha = 0.3f),
                        BlendMode.Darken,
                    ),
                    modifier = Modifier
                        .matchParentSize()
                        .blur(20.dp),
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(ReadabilityScrim),
                )
            }
        }
    }
}

/**
 * Vertical gradient that starts from a random entry of [Gradients] and moves to the next one
 * every 10 seconds.
 */
@Composable
private fun GradientPlaceholder(modifier: Modifier) {
    val size = Gradients.size
    var current by remember { mutableIntStateOf(Random.nextInt(size)) }
    val topColor = remember { Animatable(Gradients[current].first) }
    val bottomColor = remember { Animatable(Gradients[current].second) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(10_000)
            current = (current + 1) % size
        }
    }

    LaunchedEffect(current) {
        val next = Gradients[current]
        topColor.animateTo(next.first, animationSpec = tween(durationMillis = 3_000))
        bottomColor.animateTo(next.second, animationSpec = tween(durationMillis = 3_000))
    }

    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    topColor.value,
                    bottomColor.value,
                )
            )
        ),
    )
}
