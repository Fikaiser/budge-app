package hr.fika.budgeapp.balance.network

import hr.fika.budgeapp.common.BUDGE_API_URL
import hr.fika.budgeapp.common.authentication.AuthenticationHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object BalanceApiProvider {
    fun provideBalanceApi(): BalanceApi {
        return Retrofit.Builder().baseUrl(BUDGE_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(AuthenticationHelper.getTokenBearerClient())
            .build()
            .create(BalanceApi::class.java)
    }
}