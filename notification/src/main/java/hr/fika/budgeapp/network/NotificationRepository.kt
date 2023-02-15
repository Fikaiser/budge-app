package hr.fika.budgeapp.network


object NotificationRepository {
    suspend fun getPendingPaymentStatus(accountId: Int) : Boolean {
        val api = NotificationApiProvider.provideAccountApi()
        val response = api.getPendingPaymentStatus(accountId)
        return response.body()?: false
    }

    suspend fun getOverspendStatus(userId: Int) : Boolean {
        val api = NotificationApiProvider.provideAccountApi()
        val response = api.getOverspendStatus(userId)
        return response.body()?: false
    }
}