package hr.fika.budgeapp.investment.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.model.LineData
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner
import hr.fika.budgeapp.design_system.theme.budgeBlue
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton
import hr.fika.budgeapp.design_system.ui.text.RoundedText
import hr.fika.budgeapp.investment.model.*
import hr.fika.budgeapp.investment.viewmodel.InvestmentViewModel
import java.math.RoundingMode

data class TabItem(val title: String, val type: AssetType)
enum class AssetType { STOCKS, CRYPTO }

private val tabItems = listOf(
    TabItem("Stocks", AssetType.STOCKS),
    TabItem("Crypto", AssetType.CRYPTO)
)

@Composable
fun InvestmentScreen(viewModel: InvestmentViewModel = viewModel()) {
    val viewState = viewModel.viewState.observeAsState()
    var selectedTab by remember { mutableStateOf(0) }
    TabRow(modifier = Modifier.padding(top = 24.dp), selectedTabIndex = selectedTab) {
        tabItems.forEachIndexed { index, tabItem ->
            Tab(selected = selectedTab == index, onClick = {
                selectedTab = index
                viewModel.loadData(tabItem.type)
            }) {
                Text(text = tabItem.title)
            }
        }
    }
    when (viewState.value) {
        is InvestmentUiState.CRYPTO -> CryptoTable(
            (viewState.value as InvestmentUiState.CRYPTO).balances,
            viewModel
        )
        is InvestmentUiState.CRYPTO_GRAPH -> CryptoGraph(
            (viewState.value as InvestmentUiState.CRYPTO_GRAPH).history,
            (viewState.value as InvestmentUiState.CRYPTO_GRAPH).tag,
            viewModel
        )
        InvestmentUiState.ERROR -> {}
        InvestmentUiState.INITIAL -> {}
        InvestmentUiState.LOADING -> LoadingAnimation3()
        is InvestmentUiState.STOCKS -> StocksTable(
            (viewState.value as InvestmentUiState.STOCKS).balances,
            viewModel
        )
        is InvestmentUiState.STOCKS_GRAPH -> StockGraph(
            (viewState.value as InvestmentUiState.STOCKS_GRAPH).history,
            (viewState.value as InvestmentUiState.STOCKS_GRAPH).tag,
            viewModel
        )
        else -> {}
    }
}

@Composable
fun CryptoGraph(history: HistoricalCoinPrice, tag: String, viewModel: InvestmentViewModel) {
    AssetGraph(lineData = history.history.map { it.toLineData() }, tag, viewModel, AssetType.CRYPTO)
}

@Composable
fun StockGraph(history: HistoricalStockPrice, tag: String, viewModel: InvestmentViewModel) {
    AssetGraph(history.values.map { it.toLineData() }, tag, viewModel, AssetType.STOCKS)
}

@Composable
fun TimeframeDropdown(
    tag: String,
    label: String,
    viewModel: InvestmentViewModel,
    type: AssetType,
    modifier: Modifier = Modifier
) {
    val menuText = viewModel.period.value
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Column(modifier = modifier) {
        Text(modifier = Modifier.padding(start = 18.dp), text = label)
        RoundedText(text = menuText!!, onClick = { isExpanded = true })
        DropdownMenu(
            modifier = Modifier.padding(start = 18.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            val periods = InvestmentViewModel.PricePeriod.values().toList()
            periods.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.title) },
                    onClick = {
                        when (type) {
                            AssetType.STOCKS -> viewModel.getStockPriceHistory(tag, it)
                            AssetType.CRYPTO -> viewModel.getCryptoPriceHistory(tag, it)
                        }
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AssetGraph(
    lineData: List<LineData>,
    tag: String,
    viewModel: InvestmentViewModel,
    type: AssetType
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .padding(top = 64.dp)
                .align(Alignment.TopCenter),
            text = "ASSET: $tag", fontSize = 24.sp
        )
        CurveLineChart(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.Center),
            lineData = lineData,
            chartColor = Color.Green,
            lineColor = Color.LightGray,
            chartDimens = ChartDimens(32.dp)
        )
        TimeframeDropdown(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            tag = tag,
            label = "Period",
            viewModel = viewModel,
            type = type
        )
    }
}

@Composable
fun CryptoTable(cryptoBalances: List<CryptoBalance>, viewModel: InvestmentViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.91f)
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(cryptoBalances) {
            AssetItem(it.toAsset(), viewModel, AssetType.CRYPTO)
        }
    }
}

@Composable
fun StocksTable(stockBalances: List<StockBalance>, viewModel: InvestmentViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.91f)
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(stockBalances) {
            AssetItem(it.toAsset(), viewModel, AssetType.STOCKS)
        }
    }
}

@Composable
fun AssetItem(asset: Asset, viewModel: InvestmentViewModel, type: AssetType) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .clickable {
                when (type) {
                    AssetType.STOCKS -> viewModel.getStockPriceHistory(asset.tag)
                    AssetType.CRYPTO -> viewModel.getCryptoPriceHistory(asset.tag)
                }
            }
            .clip(BudgeRoundedCorner), color = budgeBlue
    ) {
        ConstraintLayout(modifier = Modifier) {
            val (tag, amount, icon, price, total) = createRefs()
            Text(
                modifier = Modifier.constrainAs(tag) {
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                },
                text = asset.tag
            )
            Text(
                modifier = Modifier.constrainAs(price) {
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(tag.end, 8.dp)
                },
                text = "${roundDouble(asset.price)}€"
            )
            Text(
                modifier = Modifier.constrainAs(amount) {
                    top.linkTo(tag.bottom, 4.dp)
                    start.linkTo(parent.start, 16.dp)
                },
                text = asset.amount.toString()
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .constrainAs(total) {
                        top.linkTo(tag.bottom, 4.dp)
                        start.linkTo(amount.end, 8.dp)
                    },
                text = "- Total: ${roundDouble(asset.amount * asset.price)}€"
            )
            AsyncImage(
                modifier = Modifier
                    .size(42.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                        bottom.linkTo(parent.bottom, 8.dp)
                    },
                model = asset.icon,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun LinkDialog(viewModel: InvestmentViewModel) {
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
                TextButton(onClick = { shouldShowDialog = false })
                { Text(text = "No") }
            }
        )
    }
}


private fun roundDouble(number: Double) = number
    .toBigDecimal()
    .setScale(2, RoundingMode.DOWN)
    .toDouble()
