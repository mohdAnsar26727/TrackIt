package track.it.app.ui.model

import track.it.app.R
import track.it.app.domain.model.TransactionForm

object TransactionFormField {
    val field by lazy {
        mapOf(
            TransactionForm.To to FormFieldConfig.InputField(
                labelResId = R.string.receiver,
                inputType = InputType.TEXT,
                iconResId = R.drawable.ic_person_svgrepo_com,
                maxLength = 50
            ),
            TransactionForm.Amount to FormFieldConfig.InputField(
                labelResId = R.string.amount,
                inputType = InputType.NUMBER,
                iconResId = R.drawable.ic_rupee
            ),
            TransactionForm.Note to FormFieldConfig.InputField(
                labelResId = R.string.note_label,
                inputType = InputType.TEXT,
                iconResId = R.drawable.ic_notes,
                minLines = 5
            ),
            TransactionForm.Date to FormFieldConfig.InputField(
                labelResId = R.string.date,
                inputType = InputType.TEXT,
                iconResId = R.drawable.baseline_calendar_month_24,
            ),
            TransactionForm.Image to FormFieldConfig.Image
        )
    }
}



