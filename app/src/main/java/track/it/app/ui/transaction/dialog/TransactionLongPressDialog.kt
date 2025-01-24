package track.it.app.ui.transaction.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import track.it.app.domain.model.Transaction
import track.it.app.ui.theme.marginMedium
import track.it.app.ui.theme.paddingMedium
import track.it.app.ui.transaction.details.TransactionCard

@Composable
fun TransactionColumn(
    longPressedTransaction: Transaction,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface,
                CardDefaults.shape
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingMedium.all),
        verticalArrangement = Arrangement.spacedBy(marginMedium)
    ) {
        TransactionCard(
            transaction = longPressedTransaction,
            onClick = {},
            onLongPress = {}
        )

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .animateContentSize()
                .align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(marginMedium)
        ) {
            if (!showConfirmation) {
                listOf(
                    Triple(
                        Icons.Filled.Edit,
                        MaterialTheme.colorScheme.surfaceContainer,
                        Color.Black
                    ),
                    Triple(
                        Icons.Filled.Delete,
                        Color.Red,
                        Color.White
                    ),
                ).forEach { (icon, containerColor, iconTint) ->
                    IconButton(
                        onClick = {
                            when (icon) {
                                Icons.Filled.Edit -> {
                                    onEdit()
                                }

                                Icons.Filled.Delete -> {
                                    showConfirmation = true
                                }
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    containerColor,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconTint
                            )
                        }

                    }
                }
            } else {
                Text(
                    text = "Delete, Are you sure?",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyMedium,
                )
                IconButton(onClick = {
                    showConfirmation = false
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
            }
        }
    }
}