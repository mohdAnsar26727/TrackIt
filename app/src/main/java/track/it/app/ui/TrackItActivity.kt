package track.it.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import track.it.app.ui.navigation.SetupNavHost
import track.it.app.ui.plans.details.PlanDetailsViewModel
import track.it.app.ui.theme.AppTheme
import track.it.app.ui.transaction.details.TransactionDetailsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TrackItActivity : ComponentActivity() {

    @Inject
    lateinit var planDetailsViewModelFactory: PlanDetailsViewModel.PlanDetailsViewModelFactory

    @Inject
    lateinit var transactionsDetailsViewModelFactory: TransactionDetailsViewModel.TransactionDetailsViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppTheme {
                SetupNavHost(navController = navController)
            }
        }
    }
}