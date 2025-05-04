package net.afanasev.otonfm.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
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
            Icon(
                imageVector = Icons.Filled.Audiotrack,
                contentDescription = "Preview",
                tint = Color.LightGray,
                modifier = Modifier.size(96.dp),
            )
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
