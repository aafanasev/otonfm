package net.afanasev.otonfm.player.ui

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.afanasev.otonfm.R
import net.afanasev.otonfm.player.ui.components.Background
import net.afanasev.otonfm.player.ui.components.DEFAULT_ARTWORK_FADE_MS
import net.afanasev.otonfm.player.ui.components.LandscapeContent
import net.afanasev.otonfm.player.ui.components.Logo
import net.afanasev.otonfm.player.ui.components.PortraitContent

/** How far below its place the header logo starts when it appears. */
private val LOGO_RISE = 16.dp

@Composable
fun PlayerBackground(
    viewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val artwork by viewModel.artworkUri.collectAsStateWithLifecycle()

    Background(
        artworkUri = artwork,
        modifier = modifier,
    )
}

@Composable
fun PlayerContent(
    viewModel: PlayerViewModel,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
) {
    val artwork by viewModel.artworkUri.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val nextTrackTitle by viewModel.nextTrackTitle.collectAsStateWithLifecycle()
    val buttonState by viewModel.buttonState.collectAsStateWithLifecycle()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val nextTrackPrefix = stringResource(R.string.player_next_track_prefix)

    // The default artwork is the logo itself, so in portrait, where the header logo sits right
    // above it, the header logo hides while it shows. It keeps its space, so the artwork doesn't
    // jump when real artwork arrives. It rises into place while fading in, and sinks back the same
    // way when the artwork falls back to the default. The exit easing mirrors the enter easing,
    // so hiding plays the showing motion in reverse. In landscape the two sit side by side, so the
    // header logo always shows.
    val showLogo = isLandscape || artwork != null
    val logoProgress by animateFloatAsState(
        targetValue = if (showLogo) 1f else 0f,
        animationSpec = tween(
            durationMillis = DEFAULT_ARTWORK_FADE_MS,
            easing = if (showLogo) LinearOutSlowInEasing else FastOutLinearInEasing,
        ),
    )
    val logo: @Composable () -> Unit = {
        Logo(
            isDarkMode = isDarkMode,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .graphicsLayer {
                    alpha = logoProgress
                    translationY = (1f - logoProgress) * LOGO_RISE.toPx()
                },
        )
    }

    if (isLandscape) {
        LandscapeContent(
            title = title,
            nextTrackTitle = nextTrackTitle,
            buttonState = buttonState,
            artworkUri = artwork,
            onPlayClick = viewModel::playPause,
            logo = logo,
            nextTrackPrefix = nextTrackPrefix,
            modifier = modifier.fillMaxHeight(),
        )
    } else {
        PortraitContent(
            title = title,
            nextTrackTitle = nextTrackTitle,
            buttonState = buttonState,
            artworkUri = artwork,
            onPlayClick = viewModel::playPause,
            logo = logo,
            nextTrackPrefix = nextTrackPrefix,
            modifier = modifier.fillMaxWidth(),
        )
    }
}
