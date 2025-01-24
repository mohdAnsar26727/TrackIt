package track.it.app.ui.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import track.it.app.ui.theme.marginExtraLarge
import track.it.app.ui.theme.marginExtraSmall
import track.it.app.ui.theme.marginLarge
import track.it.app.ui.theme.marginMedium
import track.it.app.ui.theme.marginSmall
import track.it.app.ui.theme.marginVerySmall

@Composable
fun SpacerDefault() {
    Spacer(modifier = Modifier.size(marginMedium))
}

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.size(marginSmall))
}

@Composable
fun SpacerVerySmall() {
    Spacer(modifier = Modifier.size(marginVerySmall))
}

@Composable
fun SpacerExtraSmall() {
    Spacer(modifier = Modifier.size(marginExtraSmall))
}

@Composable
fun SpacerLarge() {
    Spacer(modifier = Modifier.size(marginLarge))
}

@Composable
fun SpacerExtraLarge() {
    Spacer(modifier = Modifier.size(marginExtraLarge))
}

@Composable
fun Spacer(size: Dp) {
    Spacer(modifier = Modifier.size(size))
}
