package hr.fika.budgeapp.atms.network

import hr.fika.budgeapp.atms.model.AtmLocation

object AtmsRepository {
    suspend fun getBankAtmLocations(bankId: Int) : List<AtmLocation> {
        val api = AtmsApiProvider.provideAccountApi()
        val response = api.getBankAtmLocations(bankId)
        return response.body()?: listOf()
    }
}