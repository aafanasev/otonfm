package net.afanasev.otonfm.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun Dialog(
    content: @Composable ColumnScope.() -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(24.dp)) {
            content()
        }
    }
}

@Composable
fun DialogItem(
    @StringRes stringResId: Int,
    onClick: () -> Unit,
) {
    Text(
        stringResource(stringResId),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
    )
}