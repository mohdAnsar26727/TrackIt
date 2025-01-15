package track.it.app.ui.transaction.details

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dagger.hilt.android.EntryPointAccessors
import track.it.app.R
import track.it.app.di.TransactionDetailsViewModelFactoryProvider
import track.it.app.domain.model.Transaction
import track.it.app.domain.model.TransactionStatus
import track.it.app.ui.theme.green40
import track.it.app.ui.theme.marginDefault
import track.it.app.ui.theme.marginMinimal
import track.it.app.ui.theme.paddingDefault
import track.it.app.ui.theme.paddingMinimal
import track.it.app.ui.theme.yellow40
import track.it.app.ui.widget.ActionMenuText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(
    navController: NavController,
    transactionId: Long
) {
    val context = LocalContext.current
    val factory: TransactionDetailsViewModel.TransactionDetailsViewModelFactory = remember {
        EntryPointAccessors.fromActivity(
            context as Activity,
            TransactionDetailsViewModelFactoryProvider::class.java
        ).transactionDetailsViewModelFactory()
    }
    val viewModel: TransactionDetailsViewModel = factory.create(transactionId)
    val transactionData by viewModel.transactionById.collectAsState()
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Transaction Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    ActionMenuText(text = stringResource(R.string.edit)) {

                    }
                    ActionMenuText(text = stringResource(R.string.delete)) {

                    }
                }
            )
        }
    ) { paddingValues ->
        transactionData?.apply {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = transaction.to,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Amount: ${transaction.amount}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Date: ${transaction.createdAt}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                TransactionStatusChip(transactionStatus = transaction.status)

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Bill Images",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(paddingMinimal.horizontal)
                )

                Spacer(modifier = Modifier.height(marginDefault))

                BillImagesGrid(
                    images = transactionData!!.billImages.map { it.imageUrl },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TransactionStatusChip(transactionStatus: TransactionStatus) {
    when (transactionStatus) {
        TransactionStatus.PREDICTED -> {
            StatusChip(
                label = "Predicted",
                backgroundColor = yellow40,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        }

        TransactionStatus.PAID -> {
            StatusChip(
                label = "Paid",
                backgroundColor = green40,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun StatusChip(
    label: String,
    backgroundColor: Color,
    textColor: Color
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = textColor,
        modifier = Modifier
            .background(
                backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    )
}

@Composable
fun BillImagesGrid(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedImage by remember { mutableStateOf<String?>(null) }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(marginMinimal),
        horizontalArrangement = Arrangement.spacedBy(marginMinimal)
    ) {
        items(images) { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Bill Image",
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CardDefaults.shape)
                    .clickable {
                        selectedImage = imageUrl
                    },
                contentScale = ContentScale.FillBounds
            )
        }
    }

    selectedImage?.let {
        Dialog(
            onDismissRequest = {
                selectedImage = null
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            var scale by remember { mutableFloatStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingDefault.all)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale *= zoom
                            offset += pan
                        }
                    }
            ) {
                AsyncImage(
                    model = it,
                    contentDescription = "Zoomable Bill Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .align(Alignment.Center)
                        .graphicsLayer(
                            scaleX = maxOf(
                                1f,
                                minOf(
                                    3f,
                                    scale
                                )
                            ),
                            scaleY = maxOf(
                                1f,
                                minOf(
                                    3f,
                                    scale
                                )
                            ),
                            translationX = offset.x,
                            translationY = offset.y
                        )
                )

                CloseButton(modifier = Modifier.align(Alignment.TopEnd)) {
                    selectedImage = null
                }
            }
        }
    }
}

@Composable
fun CloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick)
            .border(
                BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceContainer
                ),
                CircleShape
            )
            .padding(paddingMinimal.all),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}


@Composable
fun TransactionCard(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongPress() }
                )
            }
            .wrapContentHeight()
            .border(
                BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceContainer
                ),
                CardDefaults.shape
            )
            .padding(paddingMinimal.all)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.to,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = transaction.note,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Amount: ${transaction.amount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = transaction.formattedTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Status Indicator
        TransactionStatusChip(transactionStatus = transaction.status)
    }
}
