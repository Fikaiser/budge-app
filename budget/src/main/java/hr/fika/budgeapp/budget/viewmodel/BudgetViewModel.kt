package hr.fika.budgeapp.budget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.fika.budgeapp.budget.model.Budget
import hr.fika.budgeapp.budget.network.BudgetRepository
import hr.fika.budgeapp.budget.ui.BudgetDatePickerState
import hr.fika.budgeapp.budget.ui.BudgetUiState
import hr.fika.budgeapp.common.analytics.AnalyticsManager
import hr.fika.budgeapp.common.analytics.model.Event
import hr.fika.budgeapp.common.bank.model.BudgetCalculationInfo
import hr.fika.budgeapp.common.user.dal.UserManager
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class BudgetViewModel : ViewModel() {
    private val _viewState = MutableLiveData<BudgetUiState>(BudgetUiState.LOADING)
    val viewState: LiveData<BudgetUiState> = _viewState
    private val _dateState = MutableLiveData<BudgetDatePickerState>(BudgetDatePickerState.HIDDEN)
    val dateState: LiveData<BudgetDatePickerState> = _dateState
    private val _date = MutableLiveData(LocalDate.now().plusMonths(1).withDayOfMonth(1))
    val date: LiveData<LocalDate> = _date
    private val _onTarget = MutableLiveData(true)
    val onTarget: LiveData<Boolean> = _onTarget

    lateinit var calculationInfo: BudgetCalculationInfo
    val textFieldValues = mutableMapOf<String, String>()

    init {
        getCalculationInfo()
    }

    private fun getCalculationInfo() {
        viewModelScope.launch {
            UserManager.getUserBankAccount()?.let { accountId ->
                val info = BudgetRepository.getCalculationInfo(accountId)
                info?.let {
                    calculationInfo = it
                    getBudgets()
                }
            }
        }
    }

    fun navigateToEditor() {
        _viewState.postValue(BudgetUiState.EDITOR)
    }

    fun toggleDatePicker() {
        val state = if (_dateState.value is BudgetDatePickerState.HIDDEN)
            BudgetDatePickerState.SHOWN else BudgetDatePickerState.HIDDEN
        _dateState.postValue(state)
    }

    fun setDate(dateMillis: Long?) {
        dateMillis?.let {
            val date = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            _date.postValue(date)
            _dateState.postValue(BudgetDatePickerState.HIDDEN)
            refreshAmount()
        }
    }

    fun refreshAmount() {
        _onTarget.postValue(calculateProjection() > getAmount())
    }

    private fun getAmount(): Double {
        val amount = textFieldValues["Amount"]
        return if (amount.isNullOrBlank()) 0.0 else amount.toDouble()
    }

    private fun calculateProjection(): Double {
        val targetDate = date.value!!
        var projection = 0.0
        for (transaction in calculationInfo.flow) {
            var comparisonDate = LocalDate.now()
            val transactionDate = Instant.ofEpochMilli(transaction.transactionTimestamp!!).atZone(
                ZoneId.systemDefault()
            ).toLocalDate()
            while (comparisonDate.isBefore(targetDate)) {
                if (transactionDate.dayOfMonth < comparisonDate.dayOfMonth) {
                    projection += transaction.amount!!
                }
                comparisonDate = comparisonDate.plusMonths(1)
            }
        }
        return projection + calculationInfo.total
    }

    fun addBudget() {
        viewModelScope.launch {
            val result = BudgetRepository.addBudget(
                Budget(
                    description = textFieldValues["Description"],
                    amount = getAmount(),
                    budgetDate = date.value!!.atStartOfDay(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli(),
                    userId = UserManager.user!!.idUser
                )
            )
            AnalyticsManager.logEvent(Event.BUDGET_CREATED)
            if (!result.isNullOrBlank()) {
                getBudgets()
            } else {
                _viewState.postValue(BudgetUiState.ERROR)
            }
        }
    }

    fun getBudgets() {
        _viewState.postValue(BudgetUiState.LOADING)
        viewModelScope.launch {
            with(UserManager) {
                val budgets = BudgetRepository.getBudgets(getUserBankAccount()!!, user!!.idUser)
                if (budgets != null) {
                    AnalyticsManager.logEvent(Event.BUDGET_LOADED)
                    _viewState.postValue(BudgetUiState.BUDGETS(budgets))
                } else {
                    _viewState.postValue(BudgetUiState.ERROR)
                }
            }
        }
    }

    fun deleteBudget(idBudget: Int) {
        _viewState.postValue(BudgetUiState.LOADING)
        viewModelScope.launch {
            val response = BudgetRepository.deleteBudget(idBudget)
            if (!response.isNullOrEmpty()) {
                getBudgets()
                return@launch
            }
            _viewState.postValue(BudgetUiState.ERROR)
        }
    }


}