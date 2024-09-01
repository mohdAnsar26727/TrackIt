package track.it.app.ui.plans

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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.NavController
import track.it.app.ui.theme.AppTypography
import kotlinx.coroutines.launch
import track.it.app.R
import track.it.app.domain.model.MockPlans
import track.it.app.domain.model.Plan
import track.it.app.ui.navigation.MainRoute
import track.it.app.ui.theme.marginMinimal
import track.it.app.ui.theme.marginNormal
import track.it.app.ui.theme.paddingMinimal
import track.it.app.ui.theme.paddingNormal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(navController: NavController) {
    val list by remember {
        mutableStateOf(
            MockPlans.plans
        )
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = MainRoute.Plans.title,
                    style = AppTypography.headlineSmall
                )
            }, navigationIcon = {
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
                    scope.launch {
                        snackbarHostState.showSnackbar("Add new Transaction")
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
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
            items(list) { plan ->
                PlanCard(plan, onClick = {
                    navController.navigate(MainRoute.PlanDetails.route + "/${plan.title}")
                })
            }
        }
    }

}

@Composable
fun PlanCard(plan: Plan, onClick: () -> Unit) {
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
                    progress = { plan.progress },
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.surface,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${(plan.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.size(marginNormal))
            BudgetText(plan)
        }
        TransactionSummary(plan = plan)
    }

}

@Composable
fun TransactionSummary(plan: Plan) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingNormal.top)
            .background(MaterialTheme.colorScheme.surfaceContainerLow, CardDefaults.shape)
            .padding(paddingMinimal.all)
    ) {
        Column(modifier = Modifier) {
            // Transaction Summary
            Text(
                text = "Transaction Summary",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Predicted Transactions: ${plan.predictedTransactions}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Paid Transactions: ${plan.paidTransactions}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Total Transactions: ${plan.totalTransactions}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        CircularProgressWithLabel(
            progress = plan.predictedTransactions.toFloat() / plan.totalTransactions,
            label = "Predicted"
        )
        Spacer(modifier = Modifier.size(marginNormal))
        CircularProgressWithLabel(
            progress = plan.paidTransactions.toFloat() / plan.totalTransactions,
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
fun BudgetText(plan: Plan) {
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
            append("Rs.${plan.initialBudget}")
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
            append("Rs.${plan.remainingAmount}")
        }
    }
    Text(text = text)
}