package track.it.app.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class FormFieldConfig {

    data class InputField(
        @StringRes val labelResId: Int,
        val inputType: InputType,
        @DrawableRes val iconResId: Int,
        val isReadOnly: Boolean = false,
        val maxLength: Int = 250,
        val minLines: Int = 1
    ) : FormFieldConfig()

    data object Image : FormFieldConfig()
}


