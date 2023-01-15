package hr.fika.budgeapp.account.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountApi {

    @POST("/register")
    suspend fun registerAccount(
        @Query("nickname") nickname: String,
        @Query("email") email: String,
        @Query("hash") hash: String
    ): Response<String>

    @POST("/login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("hash") hash: String
    ): Response<String>
}