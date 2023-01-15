package hr.fika.budgeapp.account.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.account.network.AccountRepository
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    val snackbarHostState = SnackbarHostState()
    val textFieldValues = mutableMapOf<String, String>()

    fun registerAccount() {
        val nickname = textFieldValues["Nickname"]!!
        val email = textFieldValues["Email"]!!
        val hash = textFieldValues["Password"]!!
        viewModelScope.launch {
            val result = AccountRepository.registerAccount(nickname, email, hash)
            val text = result?: "Request failed"
            snackbarHostState.showSnackbar(message = text)
        }
    }

    fun loginUser() {
        val email = textFieldValues["Email"]!!
        val hash = textFieldValues["Password"]!!
        viewModelScope.launch {
            val result = AccountRepository.loginUser( email, hash)
            val text = result?: "Request failed"
            snackbarHostState.showSnackbar(message = text)
        }
    }
}