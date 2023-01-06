package hr.fika.budgeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import hr.fika.budgeapp.ui.theme.BudgeAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = { Navigation() }
                    ) {
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Navigation() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.wallet), null) },
            label = { Text("Budget") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.monitoring), null) },
            label = { Text("Investment") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.account_balance), null) },
            label = { Text("Balance") },
            selected = true,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.location_on), null) },
            label = { Text("ATMs") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.account_circle), null) },
            label = { Text("Account") },
            selected = false,
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BudgeAppTheme {
        Greeting("Android")
    }
}