package hr.fika.budgeapp.budget.ui

import hr.fika.budgeapp.budget.model.Budget
import hr.fika.budgeapp.common.bank.model.Transaction

sealed class BudgetUiState {
    object LOADING : BudgetUiState()
    object INITIAL : BudgetUiState()
    data class BUDGETS(val budgets: List<Budget>) : BudgetUiState()
    object EDITOR : BudgetUiState()
    object ERROR : BudgetUiState()
}

sealed class BudgetDatePickerState {
    object HIDDEN : BudgetDatePickerState()
    object SHOWN : BudgetDatePickerState()
}