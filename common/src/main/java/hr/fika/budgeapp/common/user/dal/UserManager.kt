package hr.fika.budgeapp.common.user.dal

import hr.fika.budgeapp.common.user.model.User

object UserManager {
    var user: User? = null

    fun provideToken() : String {
        user?.let {
            return it.apiToken
        }
        return ""
    }
}
