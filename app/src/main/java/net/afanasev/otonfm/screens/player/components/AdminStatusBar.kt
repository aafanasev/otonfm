package net.afanasev.otonfm.screens.player.components

import android.widget.Toast
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.afanasev.otonfm.data.adminstatus.AdminStatusModel

@Composable
fun AdminStatusBar(
    adminStatus: AdminStatusModel,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val contentColor = if (isDarkMode) Color.White else Color.Black

    Row(
        modifier = modifier
            .clickable {
                Toast.makeText(context, adminStatus.text, Toast.LENGTH_LONG).show()
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (adminStatus.isUrgent) {
            Icon(
                imageVector = Icons.Filled.Campaign,
                contentDescription = "Urgent",
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = adminStatus.text,
            color = contentColor,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
        )
    }
}

@Preview
@Composable
fun AdminStatusBarPreviewLight() {
    AdminStatusBar(
        adminStatus = AdminStatusModel(text = "Welcome to Oton FM! Enjoy the music."),
        isDarkMode = false,
    )
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun AdminStatusBarPreviewDark() {
    AdminStatusBar(
        adminStatus = AdminStatusModel(text = "Welcome to Oton FM! Enjoy the music."),
        isDarkMode = true,
    )
}

@Preview
@Composable
fun AdminStatusBarPreviewUrgent() {
    AdminStatusBar(
        adminStatus = AdminStatusModel(text = "Scheduled maintenance tonight at 23:00 UTC", type = "urgent"),
        isDarkMode = false,
    )
}
