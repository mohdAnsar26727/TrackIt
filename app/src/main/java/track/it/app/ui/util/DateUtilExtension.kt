package track.it.app.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formattedDate(format: DateFormat): String {
    val formatter = SimpleDateFormat(
        format.format,
        Locale.getDefault()
    )
    return formatter.format(Date(this))
}

enum class DateFormat(val format: String) {
    DD_MM_YYYY("dd/MM/yyyy"),
    DD_MM_YYYY_DOT("dd.MM.yyyy"),
}