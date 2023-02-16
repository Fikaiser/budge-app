package hr.fika.budgeapp.budget.ui

import hr.fika.budgeapp.budget.model.BudgetProjection

sealed class BudgetUiState {
    object LOADING : BudgetUiState()
    data class BUDGETS(val budgets: List<BudgetProjection>) : BudgetUiState()
    object EDITOR : BudgetUiState()
    object ERROR : BudgetUiState()
}

sealed class BudgetDatePickerState {
    object HIDDEN : BudgetDatePickerState()
    object SHOWN : BudgetDatePickerState()
}