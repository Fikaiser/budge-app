package hr.fika.budgeapp.common.authentication

import hr.fika.budgeapp.common.user.dal.UserManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer " + UserManager.provideToken())
            .build()

        return try {
            chain.proceed(request)
        } catch (ex: java.lang.Exception) {
            Response.Builder().code(500).build()
        }
    }
}