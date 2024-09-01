package track.it.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import track.it.app.ui.details.DetailsScreen
import track.it.app.ui.plans.PlansScreen

sealed class MainRoute(val route: String, val title: String) {
    data object Plans : MainRoute("plans", "Track.It")
    data object PlanDetails : MainRoute("plan_details", "")
    data object TransactionDetails : MainRoute("transaction_details", "")
    data object AddEditPlan : MainRoute("edit_plan", "Add new plan")
    data object AddEditTransaction : MainRoute("edit_transaction", "Add new Transaction")
    data object Archive : MainRoute("archive", "Archived plans")
}

fun getTitle(route: String?): String {
    return when (route) {
        MainRoute.Plans.route -> MainRoute.Plans.title
        MainRoute.AddEditPlan.route -> MainRoute.AddEditPlan.title
        MainRoute.AddEditTransaction.route -> MainRoute.AddEditTransaction.title
        MainRoute.Archive.route -> MainRoute.Archive.title
        else -> "hmghm"
    }
}

@Composable
fun SetupNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Plans.route,
        modifier
    ) {
        //-------------------------Main Routes--------------------------------
        composable(MainRoute.Plans.route) {
            PlansScreen(navController)
        }
        composable(MainRoute.PlanDetails.route + "/{id}", arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )) {
            DetailsScreen(navController)
        }
        //-------------------------------------------------------------------

    }
}