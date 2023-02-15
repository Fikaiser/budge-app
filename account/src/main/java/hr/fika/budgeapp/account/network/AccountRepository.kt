package hr.fika.budgeapp.account.network

import hr.fika.budgeapp.common.user.model.User

object AccountRepository {
    suspend fun registerAccount(
        nickname: String,
        email: String,
        pass: String
    ) : String? {
        return AccountApiProvider
            .provideAccountApi()
            .registerAccount(nickname, email, pass)
            .body()
    }

    suspend fun loginUser(
        email: String,
        pass: String
    ) : User? {
        return try {
            AccountApiProvider
                .provideAccountApi()
                .loginUser(email, pass)
                .body()
        } catch (ex: Exception) {
            null
        }
    }
}