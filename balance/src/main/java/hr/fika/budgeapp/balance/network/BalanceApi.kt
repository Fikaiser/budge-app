package hr.fika.budgeapp.balance.network

import retrofit2.Response
import retrofit2.http.GET

interface BalanceApi {

    @GET("/test")
    suspend fun getGreet(
    ): Response<String>
}