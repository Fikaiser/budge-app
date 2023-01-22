package hr.fika.budgeapp.balance.network

import hr.fika.budgeapp.common.bank.model.BankAccount
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BalanceApi {

    @GET("/test")
    suspend fun getGreet(
    ): Response<String>

    @POST("/bankaccount")
    suspend fun linkBankAccount(
        @Query("userId") userId: Int,
        @Query("bankId") bankId: Int,
    ): Response<BankAccount>
}