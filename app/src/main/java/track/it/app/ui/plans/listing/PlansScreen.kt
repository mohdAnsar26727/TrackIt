package track.it.app.ui.plans.listing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import track.it.app.R
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanMetrics
import track.it.app.domain.model.PlanSortType
import track.it.app.domain.model.SortOrder
import track.it.app.ui.model.SortOption
import track.it.app.ui.model.toDisplayLabel
import track.it.app.ui.theme.AppTypography
import track.it.app.ui.theme.marginMedium
import track.it.app.ui.theme.marginSmall
import track.it.app.ui.theme.paddingMedium
import track.it.app.ui.widget.ActionButtons
import track.it.app.ui.widget.SpacerDefault
import track.it.app.ui.widget.SpacerExtraSmall
import track.it.app.ui.widget.SpacerSmall
import track.it.app.ui.widget.handlePagingState
import track.it.app.util.toTitleCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(
    onPlanClick: (
        id: Long,
        title: String
    ) -> Unit,
    onNewPlanClick: () -> Unit,
    myViewModel: PlanViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val sortSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val plans = myViewModel.plansFlow.collectAsLazyPagingItems(scope.coroutineContext)
    val query by myViewModel.queryFlow.collectAsStateWithLifecycle()
    val sortOption by myViewModel.sortOption.collectAsStateWithLifecycle()
    var showSortSheet by remember { mutableStateOf(false) }

    if (showSortSheet) {
        ShowSortOptionsSheet(
            sheetState = sortSheetState,
            sortOption = sortOption,
            onDismiss = {
                showSortSheet = false
            },
            onSave = { option ->
                myViewModel.updateSortOption(option)
                showSortSheet = false
            }
        )
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(paddingMedium.horizontal)
                                    .size(32.dp),
                                painter = painterResource(id = R.drawable.ic_wallet),
                                contentDescription = null
                            )
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                )
                if (plans.itemCount > 0) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query ->
                            myViewModel.updateQuery(query)
                        },
                        shape = CardDefaults.shape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        leadingIcon = {
                            IconButton(onClick = {

                            }) {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            focusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        placeholder = {
                            Text("Search...")
                        }
                    )
                    SpacerDefault()
                }
            }

        },
        bottomBar = {
            BottomAppBar(
                //containerColor = Color.Transparent
            ) {
                IconButton(
                    onClick = {
                        showSortSheet = true
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_sort_24),
                        contentDescription = null
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewPlanClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingMedium.horizontal,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(marginMedium)
        ) {

            items(
                count = plans.itemCount,
                key = { plans[it]?.plan?.id ?: 0 }
            ) { index ->
                val planDetails = plans[index] ?: return@items
                PlanCard(
                    planDetails,
                    onClick = {
                        onPlanClick(
                            planDetails.plan.id,
                            planDetails.plan.title
                        )
                    },
                )
            }

            item {
                SpacerDefault()
            }

            handlePagingState(
                plans,
                R.string.looks_like_no_plans
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSortOptionsSheet(
    sheetState: SheetState,
    sortOption: SortOption,
    onDismiss: () -> Unit,
    onSave: (SortOption) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var type: PlanSortType? = null
    var order: SortOrder? = null

    when (sortOption) {
        is SortOption.ActiveSort -> {
            type = sortOption.type
            order = sortOption.order
        }

        else -> Unit
    }

    var selectedSortType by remember { mutableStateOf(type) }
    var selectedSortOrder by remember { mutableStateOf(order) }
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        val columnModifier = Modifier
            .fillMaxWidth()
            .padding(paddingMedium.all)
            .clip(CardDefaults.shape)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest,
                CardDefaults.shape
            )
            .selectableGroup()

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sort_by),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(paddingMedium.horizontal),
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = columnModifier
            ) {
                PlanSortType.entries.forEach { sortType ->
                    SortOptionItem(sortType.toDisplayLabel(context), sortType == selectedSortType) {
                        selectedSortType = sortType
                    }
                }
            }
            SpacerDefault()
            Text(
                text = stringResource(R.string.sort_order),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(paddingMedium.horizontal),
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = columnModifier
            ) {
                SortOrder.entries.forEach { sortOrder ->
                    SortOptionItem(sortOrder.name.toTitleCase(), sortOrder == selectedSortOrder) {
                        selectedSortOrder = sortOrder
                    }
                }
            }

            Text(
                text = stringResource(R.string.plan_sort_clear_info),
                color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(paddingMedium.horizontal)
            )

            fun onClick(option: SortOption) {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onSave(option)
                    }
                }
            }

            ActionButtons(
                primaryButtonText = stringResource(R.string.save),
                secondaryButtonText = stringResource(R.string.clear),
                onPrimaryAction = {
                    onClick(
                        SortOption.ActiveSort(
                            selectedSortType ?: PlanSortType.TITLE,
                            selectedSortOrder ?: SortOrder.ASC
                        )
                    )
                },
                onSecondaryAction = {
                    onClick(SortOption.NoSort)

                }
            )
        }
    }
}

@Composable
fun SortOptionItem(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(paddingMedium.horizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        RadioButton(
            isSelected,
            onClick = onClick,
        )
    }
}

@Composable
fun PlanCard(
    planDetails: PlanDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val plan = planDetails.plan
    val metrics = planDetails.progress

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(
                BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceContainerLow
                ),
                CardDefaults.shape
            )
            .clickable(onClick = onClick)
            .padding(paddingMedium.all)
            .wrapContentHeight()
    ) {
        Text(
            text = plan.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
        )

        SpacerDefault()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            BudgetText(
                plan.initialBudget,
                metrics.remainingAmount,
                modifier = Modifier.weight(1f)
            )
            SpacerDefault()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                CircularProgressIndicator(
                    progress = { metrics.progress },
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.surface,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${(metrics.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}

@Composable
fun TransactionSummary(
    modifier: Modifier = Modifier,
    metrics: PlanMetrics,
    isExpanded: Boolean,
    onAddClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            isExpanded
        ) {
            Row {
                Column() {
                    // Transaction Summary
                    Text(
                        text = "Transaction Summary",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Estimated Transactions: ${metrics.estimatedTransactions}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    SpacerExtraSmall()
                    Text(
                        text = "Paid Transactions: ${metrics.paidTransactions}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    SpacerExtraSmall()
                    Text(
                        text = "Total Transactions: ${metrics.totalTransactions}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    SpacerExtraSmall()
                }
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressWithLabel(
                    progress = metrics.estimatedTransactionProgress,
                    label = "Estimated"
                )
                Spacer(modifier = Modifier.size(marginMedium))

                CircularProgressWithLabel(
                    progress = metrics.paidTransactionProgress,
                    label = "Paid"
                )
            }
        }

        Text(
            modifier = Modifier,
            text = "Rs. ${metrics.totalSpent}",
            style = AppTypography.titleLarge
        )
        SpacerSmall()
        Button(
            onClick = onAddClick,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            shape = OutlinedTextFieldDefaults.shape
        ) {
            Text(
                text = "Add new transaction",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun CircularProgressWithLabel(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .wrapContentSize()
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(60.dp),
                strokeWidth = 6.dp,
                trackColor = MaterialTheme.colorScheme.surface,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.size(marginSmall))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun BudgetText(
    initialBudget: Double,
    remainingAmount: Double,
    modifier: Modifier = Modifier
) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        ) {
            append("Budget: ")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        ) {
            append("Rs.${initialBudget}")
        }
        append("\n")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        ) {
            append("Remaining: ")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        ) {
            append("Rs.${remainingAmount}")
        }
    }
    Text(text = text, modifier, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
}