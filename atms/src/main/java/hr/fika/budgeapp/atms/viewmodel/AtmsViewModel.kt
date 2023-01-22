package hr.fika.budgeapp.atms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.atms.model.AtmLocation
import hr.fika.budgeapp.atms.network.AtmsRepository
import kotlinx.coroutines.launch

class AtmsViewModel : ViewModel() {
    private val _locations = MutableLiveData(listOf<AtmLocation>())
    val locations: LiveData<List<AtmLocation>> = _locations

    fun getAtmLocations() {
        viewModelScope.launch {
            val response = AtmsRepository.getBankAtmLocations(1)
            _locations.postValue(response)
        }
    }
}