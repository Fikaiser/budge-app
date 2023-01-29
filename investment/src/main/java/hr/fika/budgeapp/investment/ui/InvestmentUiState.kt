package hr.fika.budgeapp.investment.ui

import hr.fika.budgeapp.investment.model.CryptoBalance
import hr.fika.budgeapp.investment.model.HistoricalCoinPrice
import hr.fika.budgeapp.investment.model.HistoricalStockPrice
import hr.fika.budgeapp.investment.model.StockBalance

sealed class InvestmentUiState {
    object LOADING : InvestmentUiState()
    object INITIAL : InvestmentUiState()
    data class CRYPTO(val balances: List<CryptoBalance>) : InvestmentUiState()
    data class CRYPTO_GRAPH(val history: HistoricalCoinPrice) : InvestmentUiState()
    data class STOCKS(val balances: List<StockBalance>) : InvestmentUiState()
    data class STOCKS_GRAPH(val history: HistoricalStockPrice) : InvestmentUiState()
    object ERROR : InvestmentUiState()
}
