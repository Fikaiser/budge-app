package hr.fika.budgeapp.balance.network


object BalanceRepository {

    suspend fun getGreet() : String? {
        return BalanceApiProvider.provideBalanceApi().getGreet().body()
    }
}