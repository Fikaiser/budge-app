package hr.fika.budgeapp.common.authentication

import okhttp3.OkHttpClient

object AuthenticationHelper {

    fun getTokenBearerClient() : OkHttpClient {
        val interceptor = TokenInterceptor()
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

}