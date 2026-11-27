package net.afanasev.otonfm.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A translucent, clickable capsule for short labels over the player, such as the admin status and
 * the chat button. Text and icons inside take [contentColor] and the label text style.
 */
@Composable
fun Pill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides MaterialTheme.typography.labelLarge,
    ) {
        Row(
            modifier = modifier
                .clip(CircleShape)
                .background(containerColor)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
