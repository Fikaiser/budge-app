package hr.fika.budgeapp.balance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fika.budgeapp.balance.network.BalanceRepo
import hr.fika.budgeapp.balance.network.BalanceRepository
import hr.fika.budgeapp.balance.ui.BalanceUiState
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch

@HiltViewModel
class BalanceViewModel() : ViewModel() {
    private val _viewState = MutableLiveData<BalanceUiState>(BalanceUiState.INITIAL)
    val viewState: LiveData<BalanceUiState> = _viewState
    init {
        // getGreet()
    }

    fun getGreet() {
        viewModelScope.launch {
            val result = BalanceRepository.getGreet()
            val text = result?: "Request failed"
        }
    }

    fun linkBankAccount() {
        _viewState.postValue(BalanceUiState.LOADING)
        val userId = UserManager.user!!.idUser
        viewModelScope.launch {
            val repo = BalanceRepo()
            val result = BalanceRepository.linkBankAccount(userId, 1)
            _viewState.postValue(BalanceUiState.INCOME)
        }
    }
}