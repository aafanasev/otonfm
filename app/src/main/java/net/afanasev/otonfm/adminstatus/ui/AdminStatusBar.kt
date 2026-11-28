package net.afanasev.otonfm.adminstatus.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.adminstatus.data.AdminStatusModel
import net.afanasev.otonfm.common.ui.components.MarqueeText
import net.afanasev.otonfm.common.ui.components.Pill
import net.afanasev.otonfm.common.ui.style.BrandRed
import net.afanasev.otonfm.common.ui.style.OtonFmTheme

/**
 * The admin's message as one scrolling line; a tap opens it in full in a bottom sheet. An urgent
 * message turns the whole pill red.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminStatusBar(
    adminStatus: AdminStatusModel,
    modifier: Modifier = Modifier,
) {
    var showFullText by remember { mutableStateOf(false) }
    // A short message still reads as a banner, not a button.
    val pillModifier = modifier.widthIn(min = 160.dp)

    if (adminStatus.isUrgent) {
        Pill(
            onClick = { showFullText = true },
            containerColor = BrandRed,
            contentColor = Color.White,
            modifier = pillModifier,
        ) {
            Icon(
                imageVector = Icons.Filled.Campaign,
                contentDescription = "Urgent",
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            MarqueeText(text = adminStatus.text)
        }
    } else {
        Pill(onClick = { showFullText = true }, modifier = pillModifier) {
            MarqueeText(text = adminStatus.text)
        }
    }

    if (showFullText) {
        ModalBottomSheet(onDismissRequest = { showFullText = false }) {
            SelectionContainer {
                Text(
                    text = adminStatus.text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun AdminStatusBarPreviewLight() {
    OtonFmTheme(isDarkMode = false) {
        AdminStatusBar(adminStatus = AdminStatusModel(text = "Welcome to Oton FM! Enjoy the music."))
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun AdminStatusBarPreviewDark() {
    OtonFmTheme(isDarkMode = true) {
        AdminStatusBar(adminStatus = AdminStatusModel(text = "Иһиллибэт буоллаҕына VPN холбооҥ!"))
    }
}

@Preview
@Composable
private fun AdminStatusBarPreviewUrgent() {
    OtonFmTheme(isDarkMode = true) {
        AdminStatusBar(
            adminStatus = AdminStatusModel(
                text = "Scheduled maintenance tonight at 23:00 UTC",
                type = "urgent",
            ),
        )
    }
}
