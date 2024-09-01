package track.it.app.ui.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import track.it.app.ui.theme.AppTypography
import track.it.app.domain.model.MockPlans
import track.it.app.ui.plans.BudgetText
import track.it.app.ui.plans.PlanCard
import track.it.app.ui.plans.TransactionSummary
import track.it.app.ui.theme.marginNormal
import track.it.app.ui.theme.paddingMinimal
import track.it.app.ui.theme.paddingNormal

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailsScreen(navController: NavController) {

    val plan = MockPlans.plans.first()
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

    Scaffold(
        topBar = {
            MediumTopAppBar(title = {
                Text(text = plan.title, style = appBarTitleStyle)
            }, navigationIcon = {
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
            }, actions = {
                Text(
                    text = "Edit",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .clickable {

                        }
                        .padding(
                            paddingMinimal.all
                        )
                )
            }, scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {

            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingNormal.horizontal,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(marginNormal)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(marginNormal)) {
                    // Plan Overview
                    BudgetText(plan)
                    Text(
                        text = plan.description,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    TransactionSummary(plan = plan)
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

            items(MockPlans.randomPlans(100)) { plan ->
                PlanCard(plan = plan) {

                }
            }
        }

    }

}
