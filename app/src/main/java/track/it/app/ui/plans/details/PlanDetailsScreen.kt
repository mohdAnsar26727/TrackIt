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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
import track.it.app.ui.theme.marginMedium
import track.it.app.ui.theme.paddingMedium
import track.it.app.ui.theme.paddingSmall
import track.it.app.ui.transaction.details.TransactionCard
import track.it.app.ui.transaction.dialog.TransactionColumn
import track.it.app.ui.transaction.viewmodel.TransactionCUDViewmodel
import track.it.app.ui.widget.SpacerDefault
import track.it.app.ui.widget.handlePagingState

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun PlanDetailsScreen(
    navController: NavController,
    args: ScreenPlanDetails
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
                AppTypography.titleLarge
            } else {
                AppTypography.titleMedium
            }
        }
    }
    var longPressedTransaction by remember {
        mutableStateOf<Transaction?>(null)
    }

    var showMoreOptionMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = planDetails?.plan?.title ?: args.title,
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
                    IconButton(
                        onClick = {
                            showMoreOptionMenu = true
                        }
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = showMoreOptionMenu,
                        onDismissRequest = { showMoreOptionMenu = false },
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                showMoreOptionMenu = false
                                navController.navigate(ScreenAddEditPlans(id = args.id))
                            },
                            text = {
                                Text("Edit")
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                showMoreOptionMenu = false
                                viewModel.deletePlan(args.id)
                            },
                            text = {
                                Text("Delete")
                            })

                    }
                    /* ActionMenuText(text = stringResource(R.string.edit)) {

                     }
                     ActionMenuText(text = stringResource(R.string.delete)) {

                     }*/
                },
                scrollBehavior = scrollBehavior
            )
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
        val listState = rememberLazyListState()

        val isSummaryExpanded by remember {
            derivedStateOf {
                scrollBehavior.state.collapsedFraction < 0.1f
            }
        }

        val transactionBgColorValue =
            TopAppBarDefaults.mediumTopAppBarColors().scrolledContainerColor
        val transactionBgColor by remember {
            derivedStateOf {
                if (listState.firstVisibleItemIndex > 0) {
                    transactionBgColorValue
                } else {
                    Color.Transparent
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyColumn(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(marginMedium),
                state = listState
            ) {

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(marginMedium),
                        modifier = Modifier.padding(
                            paddingMedium.all
                        )
                    ) {
                        // Plan Overview
                        BudgetText(
                            plan.initialBudget,
                            metrics.remainingAmount
                        )
                        Text(
                            text = planDetails?.plan?.description.orEmpty(),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                stickyHeader {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .background(transactionBgColor)
                            .fillMaxWidth()
                            .padding(paddingSmall.all),
                        textAlign = TextAlign.Center
                    )
                }

                items(count = transactions.itemCount) { index ->
                    transactions[index]?.let {
                        TransactionCard(
                            modifier = Modifier.padding(paddingMedium.horizontal),
                            transaction = it.transaction,
                            onClick = {
                                navController.navigate(ScreenTransactionDetails(it.transaction.id))
                            },
                            onLongPress = {
                                longPressedTransaction = it.transaction
                            })
                    }
                }

                item {
                    SpacerDefault()
                }

                handlePagingState(
                    transactions,
                    R.string.no_transactions_found
                )
            }
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                TransactionSummary(
                    Modifier
                        .background(
                            if (isSummaryExpanded) {
                                TopAppBarDefaults.topAppBarColors().containerColor
                            } else {
                                TopAppBarDefaults.topAppBarColors().scrolledContainerColor
                            }
                        )
                        .padding(paddingMedium.all),
                    metrics = metrics,
                    isExpanded = isSummaryExpanded,
                    onAddClick = {
                        navController.navigate(ScreenAddEditTransactions(planId = args.id))
                    }
                )
            }
        }
    }
}
