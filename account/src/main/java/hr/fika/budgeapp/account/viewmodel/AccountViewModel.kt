package hr.fika.budgeapp.account.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.account.network.AccountRepository
import hr.fika.budgeapp.account.ui.AccountUiState
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val _viewState = MutableLiveData(AccountUiState.LOGIN)
    val viewState: LiveData<AccountUiState> = _viewState

    val textFieldValues = mutableMapOf<String, String>()

    fun registerAccount() {
        val nickname = textFieldValues["Nickname"]!!
        val email = textFieldValues["Email"]!!
        val hash = textFieldValues["Password"]!!
        viewModelScope.launch {
            val result = AccountRepository.registerAccount(nickname, email, hash)
        }
    }

    fun loginUser() {
        val email = textFieldValues["Email"]!!
        val hash = textFieldValues["Password"]!!
        _viewState.postValue(AccountUiState.LOADING)
        viewModelScope.launch {
            val result = AccountRepository.loginUser( email, hash)
            if (result != null) {
                UserManager.user = result
                _viewState.postValue(AccountUiState.LOGOUT)
                Log.d("ASDF", "User logged in")
            }
            else {
                _viewState.postValue(AccountUiState.ERROR)
            }
        }
    }
}