package hr.fika.budgeapp.account.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.account.network.AccountRepository
import hr.fika.budgeapp.account.ui.AccountUiState
import hr.fika.budgeapp.common.sharedprefs.PreferenceKeys
import hr.fika.budgeapp.common.sharedprefs.SharedPrefsManager
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val _viewState = MutableLiveData(AccountUiState.LOGIN)
    val viewState: LiveData<AccountUiState> = _viewState

    val textFieldValues = mutableMapOf<String, String>()

    fun registerAccount() {
        val nickname = textFieldValues["Nickname"]!!
        val email = textFieldValues["Email"]!!
        val pass = textFieldValues["Password"]!!
        viewModelScope.launch {
            val result = AccountRepository.registerAccount(nickname, email, pass)
        }
    }

    private fun saveLoginInfo(email: String, pass: String) {
        SharedPrefsManager.setString(PreferenceKeys.EMAIL, email)
        SharedPrefsManager.setString(PreferenceKeys.PASSWORD, pass)
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
                saveLoginInfo(email, hash)
            }
            else {
                _viewState.postValue(AccountUiState.ERROR)
            }
        }
    }
}