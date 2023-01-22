package hr.fika.budgeapp.atms.network

import hr.fika.budgeapp.atms.model.AtmLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AtmsApi {
    @GET("/atms")
    suspend fun getBankAtmLocations(
        @Query("bankId") bankId: Int,
    ): Response<List<AtmLocation>>
}