package track.it.app.ui.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import track.it.app.ui.theme.marginDefault
import track.it.app.ui.theme.marginExtraLarge
import track.it.app.ui.theme.marginLarge
import track.it.app.ui.theme.marginMinimal

@Composable
fun SpacerDefault() {
    Spacer(modifier = Modifier.size(marginDefault))
}

@Composable
fun SpacerMinimal() {
    Spacer(modifier = Modifier.size(marginMinimal))
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
