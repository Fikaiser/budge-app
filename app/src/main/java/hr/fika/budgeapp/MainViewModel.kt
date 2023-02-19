package hr.fika.budgeapp

import android.util.Log
import androidx.lifecycle.*
import hr.fika.budgeapp.account.network.AccountRepository
import hr.fika.budgeapp.common.sharedprefs.PreferenceKeys
import hr.fika.budgeapp.common.sharedprefs.SharedPrefsManager
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _userExists = MutableLiveData<Boolean?>(null)
    val userExists: LiveData<Boolean?> = _userExists

    fun logInUser() {
        val email = SharedPrefsManager.getString(PreferenceKeys.EMAIL)
        val pass = SharedPrefsManager.getString(PreferenceKeys.PASSWORD)
        if (!email.isNullOrBlank() && !pass.isNullOrBlank()) {
            viewModelScope.launch {
                AccountRepository.loginUser(email, pass)
                val result = AccountRepository.loginUser( email, pass)
                if (result != null) {
                    UserManager.user = result
                    _userExists.postValue(true)
                    Log.d("ASDF", "User logged in")
                } else {
                    _userExists.postValue(false)
                }
            }
        } else {
            _userExists.postValue(false)
        }
    }


}