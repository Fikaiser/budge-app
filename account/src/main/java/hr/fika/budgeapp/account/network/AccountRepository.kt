package hr.fika.budgeapp.account.network

object AccountRepository {
    suspend fun registerAccount(
        nickname: String,
        email: String,
        hash: String
    ) : String? {
        return AccountApiProvider
            .provideAccountApi()
            .registerAccount(nickname, email, hash)
            .body()
    }

    suspend fun loginUser(
        email: String,
        hash: String
    ) : String? {
        return  AccountApiProvider
            .provideAccountApi()
            .loginUser(email, hash)
            .body()
    }
}