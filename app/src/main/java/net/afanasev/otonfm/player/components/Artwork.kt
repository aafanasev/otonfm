package net.afanasev.otonfm.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.theme.LocalCustomColorsPalette

@Composable
fun Artwork(
    artworkUri: String?,
    modifier: Modifier,
) {
    val previewShape = RoundedCornerShape(12.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                Modifier
                    .aspectRatio(1f)
                    .shadow(
                        elevation = 6.dp,
                        shape = previewShape,
                        ambientColor = Color.DarkGray,
                        spotColor = Color.DarkGray,
                    ),
            )
            .clip(previewShape)
            .background(LocalCustomColorsPalette.current.previewBackground),
    ) {
        if (artworkUri == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.note),
                    contentDescription = "Musical note",
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(.5f)
                        .offset(x = (-10).dp, y = (-10).dp),
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "App logo",
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.5f)
                        .padding(bottom = 40.dp),
                )
            }
        } else {
            AsyncImage(
                model = artworkUri,
                contentDescription = "Preview",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
fun ArtworkNoImagePreview() {
    Artwork(
        artworkUri = null,
        modifier = Modifier.width(60.dp),
    )
}
