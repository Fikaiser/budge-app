package hr.fika.budgeapp.investment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.common.analytics.AnalyticsManager
import hr.fika.budgeapp.common.analytics.model.Event
import hr.fika.budgeapp.common.user.dal.UserManager
import hr.fika.budgeapp.investment.model.CryptoBalance
import hr.fika.budgeapp.investment.model.StockBalance
import hr.fika.budgeapp.investment.network.InvestmentRepository
import hr.fika.budgeapp.investment.ui.InvestmentUiState
import hr.fika.budgeapp.investment.ui.AssetType
import kotlinx.coroutines.launch

class InvestmentViewModel : ViewModel() {
    private val _viewState = MutableLiveData<InvestmentUiState>(InvestmentUiState.INITIAL)
    val viewState: LiveData<InvestmentUiState> = _viewState
    private var stocks = listOf<StockBalance>()
    private var crypto = listOf<CryptoBalance>()

    private val _period = MutableLiveData("Week")
    val period: LiveData<String> = _period

    fun linkCryptoWallet() {
        _viewState.postValue(InvestmentUiState.LOADING)
        val userId = UserManager.user!!.idUser
        viewModelScope.launch {
            val result = InvestmentRepository.linkCryptoWallet(userId)
            result?.let {
                UserManager.user!!.cryptoWalletId = it.idWallet
                getCryptoBalances()
            }
        }
    }

    fun getCryptoBalances() {
        _viewState.postValue(InvestmentUiState.LOADING)
        if (crypto.isEmpty()) {
            viewModelScope.launch {
                val walletId = UserManager.user!!.cryptoWalletId
                val result = InvestmentRepository.getCryptoBalances(walletId!!)
                result?.let {
                    crypto = it
                    _viewState.postValue(InvestmentUiState.CRYPTO(it))
                }
            }
        } else {
            _viewState.postValue(InvestmentUiState.CRYPTO(crypto))
        }
    }

    fun getCryptoPriceHistory(tag: String, period: PricePeriod = PricePeriod.WEEK) {
        _viewState.postValue(InvestmentUiState.LOADING)
        viewModelScope.launch {
            val result = InvestmentRepository.getCryptoPriceHistory(tag, period.value)
            result?.let {
                AnalyticsManager.logEvent(Event.CRYPTO_CHECKED)
                _period.postValue(period.title)
                _viewState.postValue(InvestmentUiState.CRYPTO_GRAPH(it, tag))
            }
        }
    }

    fun linkStockPortfolio() {
        _viewState.postValue(InvestmentUiState.LOADING)
        val userId = UserManager.user!!.idUser
        viewModelScope.launch {
            val result = InvestmentRepository.linkStockPortfolio(userId)
            result?.let {
                UserManager.user!!.stockPortfolioId = it.idStockPortfolio
                getStockBalances()
            }
        }
    }

    fun getStockBalances() {
        _viewState.postValue(InvestmentUiState.LOADING)
        if (stocks.isEmpty()) {
            viewModelScope.launch {
                val portfolioId = UserManager.user!!.stockPortfolioId
                val result = InvestmentRepository.getStockBalances(portfolioId!!)
                result?.let {
                    stocks = it
                    _viewState.postValue(InvestmentUiState.STOCKS(it))
                }
            }
        } else {
            _viewState.postValue(InvestmentUiState.STOCKS(stocks))
        }

    }

    fun getStockPriceHistory(tag: String, period: PricePeriod = PricePeriod.WEEK) {
        _viewState.postValue(InvestmentUiState.LOADING)
        viewModelScope.launch {
            val result = InvestmentRepository.getStockPriceHistory(tag, period.value)
            result?.let {
                AnalyticsManager.logEvent(Event.STOCK_CHECKED)
                _period.postValue(period.title)
                _viewState.postValue(InvestmentUiState.STOCKS_GRAPH(it, tag))
            }
        }
    }

    fun loadData(type: AssetType) {
        when (type) {
            AssetType.STOCKS -> getStockBalances()
            AssetType.CRYPTO -> getCryptoBalances()
        }
    }

    enum class PricePeriod(val title: String, val value: String) {
        WEEK("Week", "week"),
        MONTH("Month", "month"),
        YEAR("Year", "year");
    }
}