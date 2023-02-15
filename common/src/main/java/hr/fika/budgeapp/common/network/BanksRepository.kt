package hr.fika.budgeapp.common.network

import hr.fika.budgeapp.common.bank.model.Bank

object BanksRepository {
    suspend fun getBankAtmLocations() : List<Bank>? {
        return BanksApiProvider.provideAccountApi().getBanks().body()
    }
}