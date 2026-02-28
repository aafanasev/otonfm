package net.afanasev.otonfm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.afanasev.otonfm.R

@Composable
fun FlagIcon(flag: String, size: Dp) {
    if (flag == "yakutia") {
        Image(
            painter = painterResource(R.drawable.flag_yakutia),
            contentDescription = "Yakutia",
            modifier = Modifier.size(size),
        )
    } else {
        Text(text = flag, fontSize = (size / 1.dp * 0.75f).sp)
    }
}
