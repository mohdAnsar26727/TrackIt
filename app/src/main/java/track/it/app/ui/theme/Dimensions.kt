package track.it.app.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import track.it.app.ui.theme.Dimensions.extraLarge
import track.it.app.ui.theme.Dimensions.extraSmall
import track.it.app.ui.theme.Dimensions.large
import track.it.app.ui.theme.Dimensions.medium
import track.it.app.ui.theme.Dimensions.small
import track.it.app.ui.theme.Dimensions.verySmall


object Dimensions {
    val extraSmall = 2.dp
    val verySmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp
}

val marginExtraSmall = extraSmall
val marginVerySmall = verySmall
val marginSmall = small
val marginMedium = medium
val marginLarge = large
val marginExtraLarge = extraLarge

val paddingSmall = ContentPadding(small)
val paddingMedium = ContentPadding(medium)
val paddingLarge = ContentPadding(large)
val paddingExtraLarge = ContentPadding(extraLarge)

class ContentPadding(private val value: Dp) {
    val all = PaddingValues(value)
    val start = PaddingValues(start = value)
    val end = PaddingValues(end = value)
    val top = PaddingValues(top = value)
    val bottom = PaddingValues(bottom = value)
    val horizontal = PaddingValues(horizontal = value)
    val vertical = PaddingValues(vertical = value)
}
