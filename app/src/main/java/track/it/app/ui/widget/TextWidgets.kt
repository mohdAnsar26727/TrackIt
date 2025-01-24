package track.it.app.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import track.it.app.ui.theme.paddingSmall

@Composable
fun ActionMenuText(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .clickable(onClick = onClick)
            .padding(paddingSmall.all)
    )
}