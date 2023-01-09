package hr.fika.budgeapp.balance.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.fika.budgeapp.balance.viewmodel.BalanceViewModel


@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel = viewModel()
) {
    val state = viewModel.test.observeAsState()
    if (!state.value.isNullOrBlank()) {
        Text(state.value!!)
    }
}