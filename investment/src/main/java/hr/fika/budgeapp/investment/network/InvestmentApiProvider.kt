package hr.fika.budgeapp.investment.network

import hr.fika.budgeapp.common.BUDGE_API_URL
import hr.fika.budgeapp.common.authentication.AuthenticationHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object InvestmentApiProvider {
    fun provideInvestmentApi(): InvestmentApi {
        return Retrofit.Builder().baseUrl(BUDGE_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(AuthenticationHelper.getTokenBearerClient())
            .build()
            .create(InvestmentApi::class.java)
    }
}