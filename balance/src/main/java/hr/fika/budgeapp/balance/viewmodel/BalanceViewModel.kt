package hr.fika.budgeapp.balance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fika.budgeapp.balance.network.BalanceRepository
import hr.fika.budgeapp.balance.ui.BalanceUiState
import hr.fika.budgeapp.balance.ui.TransactionType
import hr.fika.budgeapp.common.analytics.AnalyticsManager
import hr.fika.budgeapp.common.analytics.model.Event
import hr.fika.budgeapp.common.bank.model.Transaction
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.random.Random

@HiltViewModel
class BalanceViewModel() : ViewModel() {
    private val _viewState = MutableLiveData<BalanceUiState>(BalanceUiState.LOADING)
    val viewState: LiveData<BalanceUiState> = _viewState

    private lateinit var innerDate: LocalDate
    private lateinit var innerTime: LocalTime
    private val _date = MutableLiveData(LocalDateTime.now())
    val date: LiveData<LocalDateTime> = _date
    private val _isRepeating = MutableLiveData(false)
    val isRepeating: LiveData<Boolean> = _isRepeating

    val textFieldValues = mutableMapOf<String, String>()

    init {
        if (UserManager.user!!.bankAccount != null) {
            getTransactions()
        } else {
            _viewState.postValue(BalanceUiState.DIALOG)
        }
    }

    fun getFlow() {
        _viewState.postValue(BalanceUiState.LOADING)
        viewModelScope.launch {
            val accountId = UserManager.user!!.bankAccount
            viewModelScope.launch {
                val result = BalanceRepository.getFlow(accountId!!.idBankAccount!!)
                result?.let {
                    AnalyticsManager.logEvent(Event.FLOW_LOADED)
                    _viewState.postValue(BalanceUiState.INCOME(it))
                }
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
                result?.let {
                    AnalyticsManager.logEvent(Event.BALANCE_LOADED)
                    _viewState.postValue(BalanceUiState.TRANSACTIONS(it))
                }
            }
        }
    }

    fun linkBankAccount() {
        _viewState.postValue(BalanceUiState.LOADING)
        val userId = UserManager.user!!.idUser
        viewModelScope.launch {
            val result = BalanceRepository.linkBankAccount(userId, Random.nextInt(1, 3))
            result?.let {
                UserManager.user!!.bankAccount = it
                getTransactions()
            }
            getTransactions()
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
            getFlow()
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
        val event = if (isRepeating.value!!) Event.FLOW_ADDED else Event.TRANSACTION_ADDED
        viewModelScope.launch {
            AnalyticsManager.logEvent(event)
            val result = BalanceRepository.saveTransaction(transaction)
            getFlow()
        }
    }

    fun loadData(type: TransactionType) {
        when (type) {
            TransactionType.TRANSACTIONS -> getTransactions()
            TransactionType.FLOW -> getFlow()
        }
    }
}