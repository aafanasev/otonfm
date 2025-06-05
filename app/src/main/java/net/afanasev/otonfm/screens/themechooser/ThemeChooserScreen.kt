package net.afanasev.otonfm.screens.themechooser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThemeChooserScreen(onThemeSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Button(onClick = {
                onThemeSelected("system")
            }) {
                Text("System")
            }

            HorizontalDivider()

            Button(onClick = {
                onThemeSelected("dark")
            }) {
                Text("Dark")
            }

            HorizontalDivider()

            Button(onClick = {
                onThemeSelected("light")
            }) {
                Text("Light")
            }
        }
    }

}