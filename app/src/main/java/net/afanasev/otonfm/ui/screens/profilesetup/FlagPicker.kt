package net.afanasev.otonfm.ui.screens.profilesetup

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.ui.components.FlagIcon

private val FLAGS = listOf(
    "yakutia", // Sakha Republic (Yakutia)
    "\uD83C\uDDF7\uD83C\uDDFA", // Russia
    "\uD83C\uDDF0\uD83C\uDDFF", // Kazakhstan
    "\uD83C\uDDFA\uD83C\uDDF8", // United States
    "\uD83C\uDDEC\uD83C\uDDE7", // United Kingdom
    "\uD83C\uDDE6\uD83C\uDDF7", // Argentina
    "\uD83C\uDDE6\uD83C\uDDF2", // Armenia
    "\uD83C\uDDE6\uD83C\uDDFA", // Australia
    "\uD83C\uDDE6\uD83C\uDDF9", // Austria
    "\uD83C\uDDE6\uD83C\uDDFF", // Azerbaijan
    "\uD83C\uDDE7\uD83C\uDDFE", // Belarus
    "\uD83C\uDDE7\uD83C\uDDEA", // Belgium
    "\uD83C\uDDE7\uD83C\uDDF7", // Brazil
    "\uD83C\uDDE8\uD83C\uDDE6", // Canada
    "\uD83C\uDDE8\uD83C\uDDF3", // China
    "\uD83C\uDDE8\uD83C\uDDF4", // Colombia
    "\uD83C\uDDE8\uD83C\uDDFF", // Czechia
    "\uD83C\uDDE9\uD83C\uDDF0", // Denmark
    "\uD83C\uDDEA\uD83C\uDDEC", // Egypt
    "\uD83C\uDDEB\uD83C\uDDEE", // Finland
    "\uD83C\uDDEB\uD83C\uDDF7", // France
    "\uD83C\uDDEC\uD83C\uDDEA", // Georgia
    "\uD83C\uDDE9\uD83C\uDDEA", // Germany
    "\uD83C\uDDEC\uD83C\uDDF7", // Greece
    "\uD83C\uDDED\uD83C\uDDFA", // Hungary
    "\uD83C\uDDEE\uD83C\uDDF3", // India
    "\uD83C\uDDEE\uD83C\uDDE9", // Indonesia
    "\uD83C\uDDEE\uD83C\uDDF7", // Iran
    "\uD83C\uDDEE\uD83C\uDDEA", // Ireland
    "\uD83C\uDDEE\uD83C\uDDF1", // Israel
    "\uD83C\uDDEE\uD83C\uDDF9", // Italy
    "\uD83C\uDDEF\uD83C\uDDF5", // Japan
    "\uD83C\uDDF1\uD83C\uDDFB", // Latvia
    "\uD83C\uDDF1\uD83C\uDDF9", // Lithuania
    "\uD83C\uDDF2\uD83C\uDDFD", // Mexico
    "\uD83C\uDDF2\uD83C\uDDE6", // Morocco
    "\uD83C\uDDF3\uD83C\uDDF1", // Netherlands
    "\uD83C\uDDF3\uD83C\uDDFF", // New Zealand
    "\uD83C\uDDF3\uD83C\uDDEC", // Nigeria
    "\uD83C\uDDF3\uD83C\uDDF4", // Norway
    "\uD83C\uDDF5\uD83C\uDDF0", // Pakistan
    "\uD83C\uDDF5\uD83C\uDDED", // Philippines
    "\uD83C\uDDF5\uD83C\uDDF1", // Poland
    "\uD83C\uDDF5\uD83C\uDDF9", // Portugal
    "\uD83C\uDDF7\uD83C\uDDF4", // Romania
    "\uD83C\uDDF8\uD83C\uDDE6", // Saudi Arabia
    "\uD83C\uDDF7\uD83C\uDDF8", // Serbia
    "\uD83C\uDDF8\uD83C\uDDEC", // Singapore
    "\uD83C\uDDF0\uD83C\uDDF7", // South Korea
    "\uD83C\uDDEA\uD83C\uDDF8", // Spain
    "\uD83C\uDDF8\uD83C\uDDEA", // Sweden
    "\uD83C\uDDE8\uD83C\uDDED", // Switzerland
    "\uD83C\uDDF9\uD83C\uDDFC", // Taiwan
    "\uD83C\uDDF9\uD83C\uDDED", // Thailand
    "\uD83C\uDDF9\uD83C\uDDF7", // Turkey
    "\uD83C\uDDE6\uD83C\uDDEA", // UAE
    "\uD83C\uDDFA\uD83C\uDDE6", // Ukraine
    "\uD83C\uDDFA\uD83C\uDDFF", // Uzbekistan
    "\uD83C\uDDFB\uD83C\uDDF3", // Vietnam
)

@Composable
fun FlagPicker(
    selectedFlag: String?,
    onFlagSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 48.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        items(FLAGS) { flag ->
            val isSelected = flag == selectedFlag
            val shape = RoundedCornerShape(8.dp)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape)
                    .then(
                        if (isSelected) {
                            Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape)
                        } else {
                            Modifier.border(1.dp, Color.Transparent, shape)
                        }
                    )
                    .clickable { onFlagSelected(flag) }
                    .padding(4.dp),
            ) {
                FlagIcon(flag, 32.dp)
            }
        }
    }
}
