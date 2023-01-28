package hr.fika.budgeapp.balance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fika.budgeapp.balance.network.BalanceRepository
import hr.fika.budgeapp.balance.ui.BalanceUiState
import hr.fika.budgeapp.common.bank.model.Transaction
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

@HiltViewModel
class BalanceViewModel() : ViewModel() {
    private val _viewState = MutableLiveData<BalanceUiState>(BalanceUiState.INITIAL)
    val viewState: LiveData<BalanceUiState> = _viewState

    private lateinit var innerDate : LocalDate
    private lateinit var innerTime : LocalTime
    private val _date = MutableLiveData(LocalDateTime.now())
    val date: LiveData<LocalDateTime> = _date
    private val _isRepeating = MutableLiveData(false)
    val isRepeating: LiveData<Boolean> = _isRepeating

    val textFieldValues = mutableMapOf<String, String>()

    init {
        UserManager.user?.let {
            it.bankAccount?.let {
                getTransactions()
            }
        }
    }

    fun getFlow() {
        _viewState.postValue(BalanceUiState.LOADING)
        viewModelScope.launch {
            val accountId = UserManager.user!!.bankAccount
            viewModelScope.launch {
                val result = BalanceRepository.getFlow(accountId!!.idBankAccount!!)
                result?.let { _viewState.postValue(BalanceUiState.INCOME(it)) }
            }
        }
    }

    fun prepEditor() {
        _viewState.postValue(BalanceUiState.EDITOR)
    }

    fun getTransactions() {
        _viewState.postValue(BalanceUiState.LOADING)
        viewModelScope.launch {
            val accountId = UserManager.user!!.bankAccount
            viewModelScope.launch {
                val result = BalanceRepository.getTransactions(accountId!!.idBankAccount!!)
                result?.let { _viewState.postValue(BalanceUiState.TRANSACTIONS(it)) }
            }
        }
    }

    fun linkBankAccount() {
        _viewState.postValue(BalanceUiState.LOADING)
        val userId = UserManager.user!!.idUser
        viewModelScope.launch {
            val result = BalanceRepository.linkBankAccount(userId, 1)
            result?.let {
                UserManager.user!!.bankAccount = it
                getTransactions()
            }
            getFlow()
        }
    }

    fun updateDate(date: LocalDate) {
        innerDate = date
    }

    fun updateTime(time: LocalTime) {
        innerTime = time
        _date.postValue(LocalDateTime.of(innerDate, innerTime))
    }

    fun updateRepeating(isChecked: Boolean) {
        _isRepeating.postValue(isChecked)
    }

    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch {
            val result = BalanceRepository.deleteTransaction(transactionId)
        }
    }

    fun saveTransaction() {
        val description = textFieldValues["Description"]!!
        val amount = textFieldValues["Amount"]!!
        val transaction = Transaction(
            description = description,
            amount = amount.toDouble(),
            transactionTimestamp = date.value!!.toInstant(ZoneOffset.MIN).toEpochMilli(),
            reoccurring = isRepeating.value,
            accountId = UserManager.user!!.bankAccount!!.idBankAccount
        )
        viewModelScope.launch {
            val result = BalanceRepository.saveTransaction(transaction)
        }
    }
}