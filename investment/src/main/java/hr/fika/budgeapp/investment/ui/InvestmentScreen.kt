package hr.fika.budgeapp.investment.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.model.LineData
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton
import hr.fika.budgeapp.investment.viewmodel.InvestmentViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun InvestmentScreen(viewModel: InvestmentViewModel = viewModel()) {
    val viewState = viewModel.viewState.observeAsState()
    Column (modifier = Modifier.fillMaxSize()) {
        when (viewState.value) {
            InvestmentUiState.LOADING -> LoadingAnimation3()
            is InvestmentUiState.CRYPTO -> {
                val transactions = (viewState.value as InvestmentUiState.CRYPTO).balances

                transactions.forEach {
                    Text(text = it.toString())
                    IconButton(onClick = { viewModel.getCryptoPriceHistory(it.tag!!) }) {
                        Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
                    }
                }
            }
            is InvestmentUiState.CRYPTO_GRAPH ->{
                val history = (viewState.value as InvestmentUiState.CRYPTO_GRAPH).history
                val points = mutableListOf<LineData>()
                history.history.forEach {
                    points.add(LineData(it.timestamp!!.toFloat(), it.price!!.toFloat()))
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    CurveLineChart(
                        modifier = Modifier
                            .size(400.dp)
                            .align(Alignment.Center),
                        lineData = points,
                        chartColor = Color.Green,
                        lineColor = Color.LightGray,
                        chartDimens = ChartDimens(32.dp)
                    )
                }
            }
            InvestmentUiState.ERROR -> TODO()
            InvestmentUiState.INITIAL -> {
                var shouldShowDialog by remember { mutableStateOf(false) }
                var isCrypto by remember { mutableStateOf(false) }
                BudgeButton(text = "Link crypto wallet") {
                    shouldShowDialog = true
                    isCrypto = true
                }
                BudgeButton(text = "Link stock portfolio") {
                    shouldShowDialog = true
                    isCrypto = false
                }
                BudgeButton(text = "Get crypto balances") {
                    viewModel.getCryptoBalances()
                }

                BudgeButton(text = "Get stock balances") {
                    viewModel.getStockBalances()
                }

                if (shouldShowDialog) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = {
                            Text(text = "Link crypto wallet")
                        },
                        text = {
                            Text(text = "Would you like to link your crypto wallet")
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                if (isCrypto) viewModel.linkCryptoWallet() else viewModel.linkStockPortfolio()
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
            is InvestmentUiState.STOCKS -> {
                val balances = (viewState.value as InvestmentUiState.STOCKS).balances

                balances.forEach {
                    Text(text = it.toString())
                    IconButton(onClick = { viewModel.getStockPriceHistory(it.tag!!) }) {
                        Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
                    }
                }
            }
            is InvestmentUiState.STOCKS_GRAPH ->{
                val history = (viewState.value as InvestmentUiState.STOCKS_GRAPH).history
                val points = mutableListOf<LineData>()
                history.values.forEach {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val date = LocalDateTime.parse(it.datetime!!, formatter)
                    points.add(LineData(date.dayOfMonth.toFloat(), it.close!!.toFloat()))
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(modifier = Modifier.align(Alignment.TopCenter), text = "ASSET: ADBE", fontSize = 24.sp)
                    CurveLineChart(
                        modifier = Modifier
                            .size(400.dp)
                            .align(Alignment.Center),
                        lineData = points,
                        chartColor = Color.Green,
                        lineColor = Color.LightGray,
                        chartDimens = ChartDimens(32.dp)
                    )
                }
            }
            else -> TODO()
        }
    }
}