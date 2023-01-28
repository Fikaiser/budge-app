package hr.fika.budgeapp.balance.network

import hr.fika.budgeapp.common.bank.model.BankAccount
import hr.fika.budgeapp.common.bank.model.Transaction
import retrofit2.Response
import retrofit2.http.*

interface BalanceApi {

    @GET("/transactions")
    suspend fun getTransactions(
        @Query("bankAccountId") bankAccountId: Int,
    ): Response<List<Transaction>>

    @POST("/transactions")
    suspend fun saveTransaction(
        @Body transaction: Transaction
    ): Response<String>

    @DELETE("/transactions")
    suspend fun deleteTransaction(
        @Query("transactionId") transactionId: Int,
    ): Response<String>

    @GET("/flow")
    suspend fun getFlow(
        @Query("bankAccountId") bankAccountId: Int,
    ): Response<List<Transaction>>

    @POST("/bankaccount")
    suspend fun linkBankAccount(
        @Query("userId") userId: Int,
        @Query("bankId") bankId: Int,
    ): Response<BankAccount>
}