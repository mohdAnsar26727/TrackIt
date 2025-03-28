package track.it.app.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import track.it.app.R
import track.it.app.ui.theme.marginMedium
import track.it.app.ui.theme.paddingMedium

@Composable
fun CancellableSaveActionButton(
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    ActionButtons(
        modifier = modifier,
        primaryButtonText = stringResource(R.string.save),
        secondaryButtonText = stringResource(R.string.cancel),
        onPrimaryAction = {
            focusManager.clearFocus(force = true)
            onPrimaryAction()
        },
        onSecondaryAction = onSecondaryAction
    )
}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(marginMedium)
    ) {
        OutlinedButton(
            onClick = onSecondaryAction,
            modifier = Modifier
                .height(80.dp)
                .weight(1f)
                .padding(paddingMedium.start)
                .padding(paddingMedium.vertical),
            shape = OutlinedTextFieldDefaults.shape
        ) {
            Text(
                text = secondaryButtonText,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Button(
            onClick = onPrimaryAction,
            modifier = Modifier
                .height(80.dp)
                .weight(1f)
                .padding(paddingMedium.end)
                .padding(paddingMedium.vertical),
            shape = OutlinedTextFieldDefaults.shape
        ) {
            Text(
                text = primaryButtonText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
