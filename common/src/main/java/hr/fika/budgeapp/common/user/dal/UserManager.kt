package hr.fika.budgeapp.common.user.dal

import hr.fika.budgeapp.common.user.model.User

object UserManager {
    var user: User? = null
    var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwLyIsImlzcyI6Imh0dHA6Ly8wLjAuMC4wOjgwODAvIiwiZXhwIjoxNjc0MjMzMDI0LCJ1c2VybmFtZSI6InVzZXJuYW1lIn0.-GgLeLVO9miMwxFfvRHEPIrTvQqketXdqx3k5Ir36GI"
    fun provideToken() : String {
        user?.let {
            return it.apiToken
        }
        return ""
    }
}