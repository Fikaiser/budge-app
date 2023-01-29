package hr.fika.budgeapp.investment.network

import hr.fika.budgeapp.investment.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface InvestmentApi {
    @POST("/wallet")
    suspend fun linkCryptoWallet(
        @Query("userId") userId: Int,
    ): Response<CryptoWallet>

    @GET("/cryptobalance")
    suspend fun getCryptoBalances(
        @Query("walletId") walletId: Int,
    ): Response<List<CryptoBalance>>

    @GET("/cryptohistory")
    suspend fun getCryptoPriceHistory(
        @Query("tag") tag: String,
        @Query("period") period: String,
    ): Response<HistoricalCoinPrice>

    @POST("/portfolio")
    suspend fun linkStockPortfolio(
        @Query("userId") userId: Int,
    ): Response<StockPortfolio>

    @GET("/stockbalance")
    suspend fun getStockBalances(
        @Query("portfolioId") portfolioId: Int,
    ): Response<List<StockBalance>>

    @GET("/stockhistory")
    suspend fun getStockPriceHistory(
        @Query("tag") tag: String,
        @Query("interval") period: String,
    ): Response<HistoricalStockPrice>
}