package net.afanasev.otonfm.screens.player.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import net.afanasev.otonfm.data.status.DEFAULT_ARTWORK_URI
import net.afanasev.otonfm.ui.theme.BACKGROUND_GRADIENTS
import kotlin.random.Random

@Composable
fun Background(
    artworkUri: String,
    modifier: Modifier,
) {
    Crossfade(
        targetState = artworkUri == DEFAULT_ARTWORK_URI,
        animationSpec = tween(1_000),
    ) { showGradient ->
        if (showGradient) {
            val size = BACKGROUND_GRADIENTS.size
            var current by remember { mutableIntStateOf(Random.nextInt(size)) }
            val topColor = remember { Animatable(BACKGROUND_GRADIENTS[current][0]) }
            val bottomColor = remember { Animatable(BACKGROUND_GRADIENTS[current][1]) }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(10_000)
                    current = (current + 1) % size
                }
            }

            LaunchedEffect(current) {
                val next = BACKGROUND_GRADIENTS[current]
                topColor.animateTo(next[0], animationSpec = tween(durationMillis = 3_000))
                bottomColor.animateTo(next[1], animationSpec = tween(durationMillis = 3_000))
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
        } else {
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
                modifier = modifier.blur(20.dp),
            )
        }
    }
}