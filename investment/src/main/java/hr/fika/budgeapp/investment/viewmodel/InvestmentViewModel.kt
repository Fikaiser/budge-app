package hr.fika.budgeapp.investment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.common.user.dal.UserManager
import hr.fika.budgeapp.investment.network.InvestmentRepository
import hr.fika.budgeapp.investment.ui.InvestmentUiState
import kotlinx.coroutines.launch

class InvestmentViewModel : ViewModel() {
    private val _viewState = MutableLiveData<InvestmentUiState>(InvestmentUiState.INITIAL)
    val viewState: LiveData<InvestmentUiState> = _viewState

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
        viewModelScope.launch {
            val walletId = UserManager.user!!.cryptoWalletId
            val result = InvestmentRepository.getCryptoBalances(walletId!!)
            result?.let { _viewState.postValue(InvestmentUiState.CRYPTO(it)) }
        }
    }

    fun getCryptoPriceHistory(tag: String) {
        _viewState.postValue(InvestmentUiState.LOADING)
        viewModelScope.launch {
            val result = InvestmentRepository.getCryptoPriceHistory(tag, "7d")
            result?.let { _viewState.postValue(InvestmentUiState.CRYPTO_GRAPH(it)) }
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
        viewModelScope.launch {
            val portfolioId = UserManager.user!!.stockPortfolioId
            val result = InvestmentRepository.getStockBalances(portfolioId!!)
            result?.let { _viewState.postValue(InvestmentUiState.STOCKS(it)) }
        }
    }

    fun getStockPriceHistory(tag: String) {
        _viewState.postValue(InvestmentUiState.LOADING)
        viewModelScope.launch {
            val result = InvestmentRepository.getStockPriceHistory(tag, "day")
            result?.let { _viewState.postValue(InvestmentUiState.STOCKS_GRAPH(it)) }
        }
    }
}