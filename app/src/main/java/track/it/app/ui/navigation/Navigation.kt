package track.it.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import track.it.app.ui.plans.create.AddEditPlanScreen
import track.it.app.ui.plans.details.PlanDetailsScreen
import track.it.app.ui.plans.listing.PlansScreen
import track.it.app.ui.transaction.create.AddEditTransactionScreen
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
        modifier
    ) {
        //-------------------------Main Routes--------------------------------
        composable<ScreenPlans> {
            PlansScreen(navController)
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
            AddEditTransactionScreen(
                navController,
                args
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