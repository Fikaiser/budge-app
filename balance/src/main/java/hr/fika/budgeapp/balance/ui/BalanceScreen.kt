package hr.fika.budgeapp.balance.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.fika.budgeapp.balance.viewmodel.BalanceViewModel
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton


@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel = viewModel()
) {
    val viewState = viewModel.viewState.observeAsState()
    when (viewState.value) {
        BalanceUiState.LOADING -> LoadingAnimation3()
        BalanceUiState.INITIAL -> {
            var shouldShowDialog by remember { mutableStateOf(false) }
            BudgeButton(text = "Link bank account") {
                shouldShowDialog = true
            }

            if (shouldShowDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    title = {
                        Text(text = "Link bank account")
                    },
                    text = {
                        Text(text = "Would you like to link your bank account")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.linkBankAccount()
                        })
                        { Text(text = "Yes") }
                    },
                    dismissButton = {
                        TextButton(onClick = {shouldShowDialog = false})
                        { Text(text = "No") }
                    }
                )
            }
        }
        is BalanceUiState.TRANSACTIONS -> {

        }
        BalanceUiState.INCOME -> {

        }
        BalanceUiState.EDITOR -> TODO()
        else -> TODO()
    }
}