package net.afanasev.otonfm.player.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.afanasev.otonfm.R
import net.afanasev.otonfm.player.ui.components.Background
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
        defaultArtworkUri = stringResource(R.string.default_artwork_uri),
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

    val logo: @Composable () -> Unit = {
        Logo(
            isDarkMode = isDarkMode,
            modifier = Modifier.fillMaxWidth(0.6f),
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
