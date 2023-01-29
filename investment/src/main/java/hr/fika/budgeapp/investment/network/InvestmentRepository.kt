package hr.fika.budgeapp.investment.network

import hr.fika.budgeapp.investment.model.*


object InvestmentRepository {

    suspend fun linkCryptoWallet(userId: Int) : CryptoWallet? {
        return InvestmentApiProvider.provideInvestmentApi().linkCryptoWallet(userId).body()
    }

    suspend fun getCryptoBalances(walletId: Int) : List<CryptoBalance>? {
        return InvestmentApiProvider.provideInvestmentApi().getCryptoBalances(walletId).body()
    }

    suspend fun getCryptoPriceHistory(tag: String, period: String) : HistoricalCoinPrice? {
        return InvestmentApiProvider.provideInvestmentApi().getCryptoPriceHistory(
            tag,
            period
        ).body()
    }

    suspend fun linkStockPortfolio(userId: Int) : StockPortfolio? {
        return InvestmentApiProvider.provideInvestmentApi().linkStockPortfolio(userId).body()
    }

    suspend fun getStockBalances(portfolioId: Int) : List<StockBalance>? {
        return InvestmentApiProvider.provideInvestmentApi().getStockBalances(portfolioId).body()
    }

    suspend fun getStockPriceHistory(tag: String, interval: String) : HistoricalStockPrice? {
        return InvestmentApiProvider.provideInvestmentApi().getStockPriceHistory(
            tag,
            interval
        ).body()
    }
}