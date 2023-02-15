package hr.fika.budgeapp.atms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.atms.model.AtmLocation
import hr.fika.budgeapp.atms.network.AtmsRepository
import hr.fika.budgeapp.common.analytics.AnalyticsManager
import hr.fika.budgeapp.common.analytics.model.Event
import hr.fika.budgeapp.common.bank.model.Bank
import hr.fika.budgeapp.common.network.BanksRepository
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch

class AtmsViewModel : ViewModel() {
    private val _locations = MutableLiveData(listOf<AtmLocation>())
    val locations: LiveData<List<AtmLocation>> = _locations
    private val _bankName = MutableLiveData("Bank")
    val bankName: LiveData<String> = _bankName
    var banks: List<Bank> = listOf()

    init {
        getBanks()
    }

    fun getAtmLocations(bank: Bank) {
        viewModelScope.launch {
            val response = AtmsRepository.getBankAtmLocations(bank.idBank)
            AnalyticsManager.logEvent(Event.ATMS_LOADED)
            _locations.postValue(response)
            _bankName.postValue(bank.name)
        }
    }

    private fun getBanks() {
        viewModelScope.launch {
            val response = BanksRepository.getBankAtmLocations()
            response?.let {
                banks = it
                UserManager.user?.bankAccount?.let {
                    getAtmLocations(banks.find { bank -> bank.idBank == it.bankId }!!)
                }
            }
        }
    }
}