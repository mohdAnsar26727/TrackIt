package track.it.app.ui.model

import track.it.app.R
import track.it.app.domain.model.PlanForm

enum class InputType {
    TEXT, NUMBER
}

object PlanFormField {
    val fields by lazy {
        mapOf(
            PlanForm.Name to FormFieldConfig.InputField(
                labelResId = R.string.plan_name_label,
                inputType = InputType.TEXT,
                iconResId = R.drawable.ic_new_plan,
                maxLength = 50
            ),
            PlanForm.Budget to FormFieldConfig.InputField(
                labelResId = R.string.budget_label,
                inputType = InputType.NUMBER,
                iconResId = R.drawable.ic_rupee
            ),
            PlanForm.Note to FormFieldConfig.InputField(
                labelResId = R.string.note_label,
                inputType = InputType.TEXT,
                iconResId = R.drawable.ic_notes,
                minLines = 5
            )
        )
    }
}


