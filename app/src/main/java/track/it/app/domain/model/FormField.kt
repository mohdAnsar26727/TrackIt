package track.it.app.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import track.it.app.R

sealed class FormField(
    @StringRes val labelResId: Int,  // Label string resource
    val inputType: InputType,
    @DrawableRes val iconResId: Int, // Icon resource ID
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val minLines: Int = 1
) {
    data object PlanName : FormField(
        labelResId = R.string.plan_name_label,
        inputType = InputType.TEXT,
        iconResId = R.drawable.ic_new_plan,
        minLength = 3,
        maxLength = 50
    )

    data object Budget : FormField(
        labelResId = R.string.budget_label,
        inputType = InputType.NUMBER,
        iconResId = R.drawable.ic_rupee
    )

    data object Note : FormField(
        labelResId = R.string.note_label,
        inputType = InputType.TEXT,
        iconResId = R.drawable.ic_notes,
        minLength = 10,
        minLines = 5
    )
}


enum class InputType {
    TEXT, NUMBER
}

