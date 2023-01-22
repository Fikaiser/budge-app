package hr.fika.budgeapp.atms.network

import hr.fika.budgeapp.common.BUDGE_API_URL
import hr.fika.budgeapp.common.authentication.AuthenticationHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object AtmsApiProvider {

    fun provideAccountApi() : AtmsApi {
        return Retrofit.Builder().baseUrl(BUDGE_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(AuthenticationHelper.getTokenBearerClient())
            .build()
            .create(AtmsApi::class.java)
    }
}