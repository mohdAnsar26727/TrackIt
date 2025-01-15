package track.it.app.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import track.it.app.ui.theme.Dimensions.extraLargeSize
import track.it.app.ui.theme.Dimensions.largeSize
import track.it.app.ui.theme.Dimensions.mediumSize
import track.it.app.ui.theme.Dimensions.smallSize

object Dimensions {
    val smallSize = 8.dp
    val mediumSize = 16.dp
    val largeSize = 24.dp
    val extraLargeSize = 32.dp
}

val marginMinimal = smallSize
val marginDefault = mediumSize
val marginLarge = largeSize
val marginExtraLarge = extraLargeSize

val paddingMinimal = ContentPadding(smallSize)
val paddingDefault = ContentPadding(mediumSize)
val paddingLarge = ContentPadding(largeSize)
val paddingExtraLarge = ContentPadding(extraLargeSize)

class ContentPadding(private val value: Dp) {
    val all = PaddingValues(value)
    val start = PaddingValues(start = value)
    val end = PaddingValues(end = value)
    val top = PaddingValues(top = value)
    val bottom = PaddingValues(bottom = value)
    val horizontal = PaddingValues(horizontal = value)
    val vertical = PaddingValues(vertical = value)
}
