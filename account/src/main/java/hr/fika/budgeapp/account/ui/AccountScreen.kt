package hr.fika.budgeapp.account.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val screenState = viewModel.viewState.observeAsState()
        when (screenState.value) {
            AccountUiState.LOADING -> LoadingAnimation3()
            AccountUiState.LOGIN -> LoginForm(viewModel = viewModel)
            AccountUiState.REGISTER -> RegistrationForm(viewModel = viewModel)
            AccountUiState.LOGOUT -> {}
            else -> {}
        }
    }
}

@Composable
fun RegistrationForm(viewModel: AccountViewModel) {
    RoundedTextField("Email", viewModel)
    RoundedTextField("Nickname", viewModel)
    RoundedTextField("Password", viewModel, transformation = PasswordVisualTransformation())
    RoundedTextField("Repeat password", viewModel, transformation = PasswordVisualTransformation())
    BudgeButton("Register") { viewModel.registerAccount() }
}

@Composable
fun LoginForm(viewModel: AccountViewModel) {
    RoundedTextField("Email", viewModel)
    RoundedTextField("Password", viewModel, transformation = PasswordVisualTransformation())
    BudgeButton("Log In") { viewModel.loginUser() }
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