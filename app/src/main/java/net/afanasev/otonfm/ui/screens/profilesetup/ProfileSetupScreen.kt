package net.afanasev.otonfm.ui.screens.profilesetup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.afanasev.otonfm.R

@Composable
fun ProfileSetupScreen(
    initialName: String = "",
    initialFlag: String? = null,
    submitEnabled: Boolean = true,
    onProfileSetup: (displayName: String, countryFlag: String) -> Unit,
) {
    var displayName by rememberSaveable { mutableStateOf(initialName) }
    var selectedFlag by rememberSaveable { mutableStateOf(initialFlag) }
    val isValid = displayName.isNotBlank() && selectedFlag != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.profile_setup_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it.take(30) },
            label = { Text(stringResource(R.string.profile_setup_name_hint)) },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.profile_setup_pick_flag),
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlagPicker(
            selectedFlag = selectedFlag,
            onFlagSelected = { selectedFlag = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onProfileSetup(displayName.trim(), selectedFlag!!) },
            enabled = isValid && submitEnabled,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.profile_setup_submit))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(onProfileSetup = { _, _ -> })
}
