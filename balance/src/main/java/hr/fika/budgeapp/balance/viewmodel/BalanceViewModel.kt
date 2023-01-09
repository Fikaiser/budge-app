package hr.fika.budgeapp.balance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fika.budgeapp.balance.network.BalanceRepository
import kotlinx.coroutines.launch

@HiltViewModel
class BalanceViewModel : ViewModel() {
    private val _test = MutableLiveData("")
    val test: LiveData<String> = _test

    init {
        getGreet()
    }

    fun getGreet() {
        viewModelScope.launch {
            val result = BalanceRepository.getGreet()
            val text = result?: "Request failed"
            _test.postValue(text)
        }
    }
}