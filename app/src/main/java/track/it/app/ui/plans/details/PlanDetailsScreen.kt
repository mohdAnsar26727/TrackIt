package track.it.app.ui.plans.details

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.EntryPointAccessors
import track.it.app.R
import track.it.app.di.PlanDetailsViewModelFactoryProvider
import track.it.app.domain.model.Transaction
import track.it.app.ui.navigation.ScreenAddEditPlans
import track.it.app.ui.navigation.ScreenAddEditTransactions
import track.it.app.ui.navigation.ScreenPlanDetails
import track.it.app.ui.navigation.ScreenTransactionDetails
import track.it.app.ui.plans.listing.BudgetText
import track.it.app.ui.plans.listing.TransactionSummary
import track.it.app.ui.theme.AppTypography
import track.it.app.ui.theme.marginDefault
import track.it.app.ui.theme.paddingDefault
import track.it.app.ui.theme.paddingMinimal
import track.it.app.ui.transaction.details.TransactionCard
import track.it.app.ui.transaction.dialog.TransactionColumn
import track.it.app.ui.transaction.viewmodel.TransactionCUDViewmodel
import track.it.app.ui.widget.ActionMenuText
import track.it.app.ui.widget.handlePagingState

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun PlanDetailsScreen(
    navController: NavController,
    args: ScreenPlanDetails,

    ) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val factory: PlanDetailsViewModel.PlanDetailsViewModelFactory = remember {
        EntryPointAccessors.fromActivity(
            context as Activity,
            PlanDetailsViewModelFactoryProvider::class.java
        ).planDetailsViewModelFactory()
    }
    val viewModel: PlanDetailsViewModel = factory.create(args.id)
    val transactionCUDViewmodel: TransactionCUDViewmodel = hiltViewModel()

    val planDetails by viewModel.planById.collectAsState()
    val transactions = viewModel.transactions.collectAsLazyPagingItems(scope.coroutineContext)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val appBarTitleStyle by remember {
        derivedStateOf {
            if (scrollBehavior.state.collapsedFraction < 0.1f) {
                AppTypography.headlineLarge
            } else {
                AppTypography.headlineSmall
            }
        }
    }
    var longPressedTransaction by remember {
        mutableStateOf<Transaction?>(null)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = args.title,
                        style = appBarTitleStyle
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    ActionMenuText(text = stringResource(R.string.edit)) {
                        navController.navigate(ScreenAddEditPlans(id = args.id))
                    }
                    ActionMenuText(text = stringResource(R.string.delete)) {

                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(ScreenAddEditTransactions(planId = args.id))
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->

        if (longPressedTransaction != null) {
            Dialog(
                onDismissRequest = {
                    longPressedTransaction = null
                },
                properties = DialogProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                )
            ) {
                TransactionColumn(
                    longPressedTransaction = longPressedTransaction!!,
                    onEdit = {
                        navController.navigate(
                            ScreenAddEditTransactions(
                                longPressedTransaction!!.id,
                                planId = args.id
                            )
                        )
                        longPressedTransaction = null
                    },
                    onDelete = {
                        transactionCUDViewmodel.deleteTransaction(longPressedTransaction!!.id)
                        longPressedTransaction = null
                    })
            }
        }

        val plan = planDetails?.plan ?: return@Scaffold
        val metrics = planDetails?.progress ?: return@Scaffold
        LazyColumn(
            contentPadding = paddingDefault.horizontal,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(marginDefault)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(marginDefault)) {
                    // Plan Overview
                    BudgetText(
                        plan.initialBudget,
                        metrics.remainingAmount
                    )
                    Text(
                        text = planDetails?.plan?.description.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    TransactionSummary(metrics = metrics)
                }
            }

            stickyHeader {
                Text(
                    text = "Transactions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .padding(paddingMinimal.vertical)
                )
            }

            items(count = transactions.itemCount) { index ->
                transactions[index]?.let {
                    TransactionCard(
                        transaction = it.transaction,
                        onClick = {
                            navController.navigate(ScreenTransactionDetails(it.transaction.id))
                        },
                        onLongPress = {
                            longPressedTransaction = it.transaction
                        })
                }
            }

            handlePagingState(
                transactions,
                R.string.no_transactions_found
            )
        }

    }
}
