package hr.fika.budgeapp.common.network

import hr.fika.budgeapp.common.bank.model.Bank
import retrofit2.Response
import retrofit2.http.GET

interface BanksApi {
    @GET("/banks")
    suspend fun getBanks(
    ): Response<List<Bank>>
}