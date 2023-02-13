package hr.fika.budgeapp.account.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.fika.budgeapp.account.viewmodel.AccountViewModel
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton

@Preview
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel()
) {
    val screenState = viewModel.viewState.observeAsState()
    when (screenState.value) {
        AccountUiState.LOADING -> LoadingAnimation3()
        AccountUiState.LOGIN -> LoginForm(viewModel = viewModel)
        AccountUiState.REGISTER -> RegistrationForm(viewModel = viewModel)
        AccountUiState.LOGOUT -> LogoutScreen(viewModel = viewModel)
        else -> {}
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm(viewModel: AccountViewModel) {
    var dialogVisible by remember {
        mutableStateOf(false)
    }
    if (dialogVisible) {
        AlertDialog(
            modifier = Modifier,
            onDismissRequest = { dialogVisible = false }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Terms and conditions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        modifier = Modifier,
                        text = "The Budge system handles information about your financial activity such as your bank transactions or crypto and stock trades." +
                                " The app also uses an analytics system which collects anonymous information about the way you use the app which is used to further improve the user experience." +
                                " By accepting the terms you agree that Budge may collect and use the described data"
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            RoundedTextField("Email", viewModel)
            RoundedTextField("Nickname", viewModel)
            RoundedTextField("Password", viewModel, transformation = PasswordVisualTransformation())
            RoundedTextField("Repeat password", viewModel, transformation = PasswordVisualTransformation())
            LabeledCheckBox(label = "I accept the terms and conditions" )
            BudgeButton("Register") { viewModel.registerAccount() }
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable { viewModel.navigateToLogin() },
            text = "Have an account? Log in",
            color = Color.Blue
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .clickable { dialogVisible = true },
            text = "Terms and conditions",
            color = Color.Blue
        )


    }
}

@Composable
fun LoginForm(viewModel: AccountViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            RoundedTextField("Email", viewModel)
            RoundedTextField(
                "Password",
                viewModel,
                transformation = PasswordVisualTransformation()
            )
            BudgeButton("Log In") { viewModel.loginUser() }
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable { viewModel.navigateToRegister() },
            text = "Don't have an account? Register",
            color = Color.Blue
        )
    }
}

@Composable
fun LogoutScreen(viewModel: AccountViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier,
                painter = rememberVectorPainter(image = Icons.Rounded.AccountCircle),
                contentDescription = null)
            Text(
                modifier = Modifier,
                text = "Hello ${viewModel.getUsername()}",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            BudgeButton("Log out") { viewModel.logoutUser() }
        }
    }
}

@Composable
fun LabeledCheckBox(label: String) {
    var isChecked by remember {
        mutableStateOf(false)
    }
    Row (verticalAlignment = Alignment.CenterVertically) {
        Text(text = label)
        Checkbox(
            checked = isChecked,
            onCheckedChange = {newValue ->
                isChecked = newValue
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedTextField(
    placeholder: String,
    viewModel: AccountViewModel,
    modifier: Modifier = Modifier,
    transformation : VisualTransformation = VisualTransformation.None,
) {
    var value by remember { mutableStateOf("") }

    TextField(
        modifier = modifier
            .padding(top = 10.dp)
            .clip(BudgeRoundedCorner),
        value = value,
        placeholder = {Text(text = placeholder)},
        visualTransformation = transformation,
        onValueChange =  {
            value = it
            viewModel.textFieldValues[placeholder] = it
        }
    )
}