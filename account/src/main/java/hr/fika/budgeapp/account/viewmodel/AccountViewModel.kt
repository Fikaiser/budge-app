package hr.fika.budgeapp.account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.account.network.AccountRepository
import hr.fika.budgeapp.account.ui.AccountUiState
import hr.fika.budgeapp.common.analytics.AnalyticsManager
import hr.fika.budgeapp.common.analytics.model.Event
import hr.fika.budgeapp.common.sharedprefs.PreferenceKeys
import hr.fika.budgeapp.common.sharedprefs.SharedPrefsManager
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val _viewState = MutableLiveData(AccountUiState.LOADING)
    val viewState: LiveData<AccountUiState> = _viewState

    val textFieldValues = mutableMapOf<String, String>()

    init {
        chooseUiState()
    }

    private fun chooseUiState() {
        if (UserManager.user == null) {
            _viewState.postValue(AccountUiState.LOGIN)
            return
        }
        _viewState.postValue(AccountUiState.LOGOUT)
    }

    fun navigateToRegister() {
        _viewState.postValue(AccountUiState.REGISTER)
    }

    fun navigateToLogin() {
        _viewState.postValue(AccountUiState.LOGIN)
    }

    fun getUsername() = UserManager.user!!.nickname

    fun registerAccount() {
        _viewState.postValue(AccountUiState.LOADING)
        val nickname = textFieldValues["Nickname"]!!
        val email = textFieldValues["Email"]!!
        val pass = textFieldValues["Password"]!!
        viewModelScope.launch {
            val result = AccountRepository.registerAccount(nickname, email, pass)
            if (result != null) {
                AnalyticsManager.logEvent(Event.REGISTRATION)
                _viewState.postValue(AccountUiState.LOGIN)
            }
            _viewState.postValue(AccountUiState.ERROR)
        }
    }

    private fun saveLoginInfo(email: String, pass: String) {
        SharedPrefsManager.setString(PreferenceKeys.EMAIL, email)
        SharedPrefsManager.setString(PreferenceKeys.PASSWORD, pass)
    }
    private fun clearLoginInfo() {
        SharedPrefsManager.clearString(PreferenceKeys.EMAIL)
        SharedPrefsManager.clearString(PreferenceKeys.PASSWORD)
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
                AnalyticsManager.logEvent(Event.LOGIN)
                saveLoginInfo(email, hash)
            }
            else {
                _viewState.postValue(AccountUiState.ERROR)
            }
        }
    }

    fun logoutUser() {
        clearLoginInfo()
        UserManager.user = null
        _viewState.postValue(AccountUiState.LOGIN)
    }
}