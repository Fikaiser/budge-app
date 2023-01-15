package hr.fika.budgeapp.account.network

import hr.fika.budgeapp.common.BUDGE_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object AccountApiProvider {
    fun provideAccountApi() : AccountApi {
        return Retrofit.Builder().baseUrl(BUDGE_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountApi::class.java)
    }
}