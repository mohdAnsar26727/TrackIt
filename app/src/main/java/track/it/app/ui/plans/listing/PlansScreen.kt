package track.it.app.ui.plans.listing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import track.it.app.R
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanMetrics
import track.it.app.ui.navigation.ScreenAddEditPlans
import track.it.app.ui.navigation.ScreenPlanDetails
import track.it.app.ui.theme.marginMedium
import track.it.app.ui.theme.marginSmall
import track.it.app.ui.theme.paddingMedium
import track.it.app.ui.theme.paddingSmall
import track.it.app.ui.widget.SpacerDefault
import track.it.app.ui.widget.SpacerExtraSmall
import track.it.app.ui.widget.handlePagingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(
    navController: NavController,
    myViewModel: PlanViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val plans = myViewModel.plansFlow.collectAsLazyPagingItems(scope.coroutineContext)
    val query by myViewModel.queryFlow.collectAsStateWithLifecycle()

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
                    TextField(
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
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        placeholder = {
                            Text("Search...")
                        }
                    )
                    SpacerDefault()
                }
            }

        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenAddEditPlans())
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
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
                        navController.navigate(
                            ScreenPlanDetails(
                                planDetails.plan.id,
                                planDetails.plan.title
                            )
                        )
                    },
                )
            }

            handlePagingState(
                plans,
                R.string.looks_like_no_plans
            )
        }
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
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotationAnimation"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick)
            .padding(paddingMedium.all)
            .wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(paddingSmall.horizontal)
        ) {
            Text(
                text = plan.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    isExpanded = !isExpanded
                },
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        CircleShape
                    )
                    .rotate(rotationAngle)
            ) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        SpacerDefault()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = CardDefaults.shape
                )
                .padding(paddingMedium.all)
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
        AnimatedVisibility(isExpanded) {
            TransactionSummary(metrics = metrics)
        }
    }

}

@Composable
fun TransactionSummary(metrics: PlanMetrics) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingMedium.top)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLow,
                CardDefaults.shape
            )
            .padding(paddingMedium.all)
    ) {
        Column(modifier = Modifier) {
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
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.size(marginSmall))
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
                fontSize = MaterialTheme.typography.titleMedium.fontSize
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
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        ) {
            append("Rs.${remainingAmount}")
        }
    }
    Text(text = text, modifier)
}