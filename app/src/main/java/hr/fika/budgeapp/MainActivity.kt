package hr.fika.budgeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hr.fika.budgeapp.account.network.AccountRepository
import hr.fika.budgeapp.account.ui.AccountScreen
import hr.fika.budgeapp.atms.ui.AtmsScreen
import hr.fika.budgeapp.balance.ui.BalanceScreen
import hr.fika.budgeapp.common.sharedprefs.PreferenceKeys
import hr.fika.budgeapp.common.sharedprefs.SharedPrefsManager
import hr.fika.budgeapp.common.user.dal.UserManager
import hr.fika.budgeapp.ui.theme.BudgeAppTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefsManager.init(this)
        logInPreviousUser()
        setContent {
            val navController = rememberNavController()
            BudgeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = { Navigation(navController) }
                    ) {
                        NavHost(navController = navController, startDestination = "balance") {
                            composable("budget") { Greeting("Budget") }
                            composable("investment") { Greeting("Invest") }
                            composable("balance") { BalanceScreen() }
                            composable("atms") { AtmsScreen() }
                            composable("account") { AccountScreen() }
                        }
                    }
                }
            }
        }
    }

    private fun logInPreviousUser() {
        val email = SharedPrefsManager.getString(PreferenceKeys.EMAIL)
        val pass = SharedPrefsManager.getString(PreferenceKeys.PASSWORD)
        if (!email.isNullOrBlank() && !pass.isNullOrBlank()) {
            lifecycleScope.launch {
                AccountRepository.loginUser(email, pass)
                val result = AccountRepository.loginUser( email, pass)
                if (result != null) {
                    UserManager.user = result
                    Log.d("ASDF", "User logged in")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconId: Int
    ) {
    object Budget : Screen("budget", R.string.budget, R.drawable.wallet)
    object Investment : Screen("investment", R.string.investment, R.drawable.monitoring)
    object Balance : Screen("balance", R.string.balance, R.drawable.balance)
    object Atms : Screen("atms", R.string.atms, R.drawable.location_on)
    object Account : Screen("account", R.string.account, R.drawable.account_circle)
}

val items = listOf(
    Screen.Budget,
    Screen.Investment,
    Screen.Balance,
    Screen.Atms,
    Screen.Account
)

@Composable
fun Navigation(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = screen.iconId), contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BudgeAppTheme {
        Greeting("Android")
    }
}