package hr.fika.budgeapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {
    @GET("/payment")
    suspend fun getPendingPaymentStatus(
        @Query("accountId") accountId: Int
    ): Response<Boolean>

    @GET("/overspend")
    suspend fun getOverspendStatus(
        @Query("userId") userId: Int
    ): Response<Boolean>
}