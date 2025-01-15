package track.it.app.util

fun String.toTitleCase(): String {
    return this.lowercase().split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}