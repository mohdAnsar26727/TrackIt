package track.it.app.ui.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import track.it.app.domain.model.ValidationResult
import track.it.app.ui.model.FormFieldConfig
import track.it.app.ui.model.InputType

@Composable
fun FormInputField(
    config: FormFieldConfig,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    validation: ValidationResult? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    if (config !is FormFieldConfig.InputField) return

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    val context = LocalContext.current
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= config.maxLength) {
                onValueChange(it)
            }
        },
        label = {
            Text(
                context.getString(config.labelResId),
                style = MaterialTheme.typography.labelLarge
            )
        },  // Fetch label dynamically
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        keyboardOptions = when (config.inputType) {
            InputType.TEXT -> KeyboardOptions.Default
            InputType.NUMBER -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        },
        leadingIcon = {
            LeadingIcon(config, validation)
        },
        minLines = config.minLines,
        isError = validation is ValidationResult.Error,
        supportingText = {
            if (validation is ValidationResult.Error && isFocused.not()) {
                Text(validation.errorMessage, style = MaterialTheme.typography.titleSmall)
            }
        },
        trailingIcon = trailingContent,
        textStyle = MaterialTheme.typography.titleMedium,
        readOnly = config.isReadOnly,
        interactionSource = interactionSource,
    )
}

@Composable
fun LeadingIcon(config: FormFieldConfig.InputField, validation: ValidationResult?) {
    Icon(
        painter = painterResource(id = config.iconResId),
        contentDescription = null,
        tint = if (validation is ValidationResult.Error) {
            OutlinedTextFieldDefaults.colors().errorSupportingTextColor
        } else {
            OutlinedTextFieldDefaults.colors().focusedTextColor
        }
    )
}

