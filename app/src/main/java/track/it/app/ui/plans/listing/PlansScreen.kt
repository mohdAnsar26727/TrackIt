package track.it.app.ui.plans.listing

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import track.it.app.R
import track.it.app.domain.model.PlanDetails
import track.it.app.domain.model.PlanMetrics
import track.it.app.ui.navigation.ScreenAddEditPlans
import track.it.app.ui.navigation.ScreenPlanDetails
import track.it.app.ui.theme.AppTypography
import track.it.app.ui.theme.marginMinimal
import track.it.app.ui.theme.marginNormal
import track.it.app.ui.theme.paddingMinimal
import track.it.app.ui.theme.paddingNormal
import track.it.app.ui.widget.handlePagingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(
    navController: NavController,
    myViewModel: PlanViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val plans = myViewModel.plans.collectAsLazyPagingItems(scope.coroutineContext)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Track.It",
                        style = AppTypography.headlineSmall
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(paddingNormal.horizontal)
                            .size(40.dp),
                        painter = painterResource(id = R.drawable.ic_wallet),
                        contentDescription = null
                    )
                })

        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingNormal.all,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(marginNormal)
        ) {
            items(
                count = plans.itemCount
            ) { index ->
                plans[index]?.let {
                    val plan = it.plan
                    PlanCard(
                        it,
                        onClick = {
                            navController.navigate(
                                ScreenPlanDetails(
                                    plan.id,
                                    plan.title
                                )
                            )
                        })
                }
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
    onClick: () -> Unit
) {
    val plan = planDetails.plan
    val metrics = planDetails.progress
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick)
            .padding(paddingNormal.all)
            .wrapContentHeight()
    ) {
        Text(
            text = plan.title,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.size(marginNormal))
        Row(verticalAlignment = Alignment.CenterVertically) {
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
            Spacer(modifier = Modifier.size(marginNormal))
            BudgetText(
                plan.initialBudget,
                metrics.remainingAmount
            )
        }
        TransactionSummary(metrics = metrics)
    }

}

@Composable
fun TransactionSummary(metrics: PlanMetrics) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingNormal.top)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLow,
                CardDefaults.shape
            )
            .padding(paddingMinimal.all)
    ) {
        Column(modifier = Modifier) {
            // Transaction Summary
            Text(
                text = "Transaction Summary",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Predicted Transactions: ${metrics.predictedTransactions}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Paid Transactions: ${metrics.paidTransactions}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Total Transactions: ${metrics.totalTransactions}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        CircularProgressWithLabel(
            progress = metrics.predictedTransactions.toFloat() / metrics.totalTransactions,
            label = "Predicted"
        )
        Spacer(modifier = Modifier.size(marginNormal))
        CircularProgressWithLabel(
            progress = metrics.paidTransactions.toFloat() / metrics.totalTransactions,
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
        Spacer(modifier = Modifier.size(marginMinimal))
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
    remainingAmount: Double
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
    Text(text = text)
}