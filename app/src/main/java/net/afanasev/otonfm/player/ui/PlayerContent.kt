package net.afanasev.otonfm.player.ui

import android.content.res.Configuration
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.afanasev.otonfm.R
import net.afanasev.otonfm.player.ui.components.Background
import net.afanasev.otonfm.player.ui.components.DEFAULT_ARTWORK_FADE_MS
import net.afanasev.otonfm.player.ui.components.DEFAULT_ARTWORK_URI
import net.afanasev.otonfm.player.ui.components.LandscapeContent
import net.afanasev.otonfm.player.ui.components.Logo
import net.afanasev.otonfm.player.ui.components.PortraitContent

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
    val configuration = LocalConfiguration.current
    val nextTrackPrefix = stringResource(R.string.player_next_track_prefix)

    // The default artwork is the logo itself, so the header logo fades out while it shows. It
    // keeps its space, so the artwork doesn't jump when real artwork arrives.
    val logoAlpha by animateFloatAsState(
        targetValue = if (artwork == DEFAULT_ARTWORK_URI) 0f else 1f,
        animationSpec = tween(DEFAULT_ARTWORK_FADE_MS),
    )
    val logo: @Composable () -> Unit = {
        Logo(
            isDarkMode = isDarkMode,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .graphicsLayer { alpha = logoAlpha },
        )
    }

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
