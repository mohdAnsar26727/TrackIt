package track.it.app.ui.navigation

import android.app.Activity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dagger.hilt.android.EntryPointAccessors
import kotlinx.serialization.Serializable
import track.it.app.di.AddEditTransactionViewModelFactoryProvider
import track.it.app.ui.plans.create.AddEditPlanScreen
import track.it.app.ui.plans.details.PlanDetailsScreen
import track.it.app.ui.plans.listing.PlansScreen
import track.it.app.ui.transaction.create.AddEditTransactionScreen
import track.it.app.ui.transaction.create.AddEditTransactionViewModel
import track.it.app.ui.transaction.details.TransactionDetailsScreen

@Serializable
object ScreenPlans

@Serializable
data class ScreenPlanDetails(
    val id: Long,
    val title: String
)

@Serializable
data class ScreenTransactionDetails(val id: Long)

@Serializable
data class ScreenAddEditPlans(val id: Long? = null)

@Serializable
data class ScreenAddEditTransactions(
    val id: Long? = null,
    val planId: Long
)

@Serializable
data object ScreenArchivedPlans


@Composable
fun SetupNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = ScreenPlans,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                initialOffsetX = { fullWidth -> fullWidth }
            )
        },
        exitTransition = {
            fadeOut(targetAlpha = 1f)
        },
        popEnterTransition = {
            fadeIn(initialAlpha = 1f)
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                targetOffsetX = { fullWidth -> fullWidth }
            )
        }
    ) {
        //-------------------------Main Routes--------------------------------
        composable<ScreenPlans> {
            PlansScreen(
                onPlanClick = { id, title ->
                    val args = ScreenPlanDetails(id, title)
                    navController.navigate(args)
                },
                onNewPlanClick = {
                    navController.navigate(ScreenAddEditPlans())
                }
            )
        }
        composable<ScreenPlanDetails> {
            val args = it.toRoute<ScreenPlanDetails>()
            PlanDetailsScreen(
                navController,
                args
            )
        }
        composable<ScreenTransactionDetails> {
            val args = it.toRoute<ScreenTransactionDetails>()
            TransactionDetailsScreen(
                navController,
                args.id
            )
        }
        composable<ScreenAddEditTransactions> {
            val args = it.toRoute<ScreenAddEditTransactions>()

            val context = LocalContext.current
            val factory: AddEditTransactionViewModel.AddEditTransactionViewModelFactory = remember {
                EntryPointAccessors.fromActivity(
                    context as Activity,
                    AddEditTransactionViewModelFactoryProvider::class.java
                ).addEditTransactionViewModelFactory()
            }
            val viewModel: AddEditTransactionViewModel = factory.create(args.planId)

            AddEditTransactionScreen(
                navController,
                args,
                viewModel = viewModel
            )
        }
        composable<ScreenAddEditPlans> {
            val args = it.toRoute<ScreenAddEditPlans>()
            AddEditPlanScreen(
                navController,
                args.id
            )
        }

        //-------------------------------------------------------------------
    }
}
