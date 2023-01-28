package hr.fika.budgeapp.balance.ui

import hr.fika.budgeapp.common.bank.model.Transaction

sealed class BalanceUiState {
    object LOADING : BalanceUiState()
    object INITIAL : BalanceUiState()
    data class TRANSACTIONS(val transactions: List<Transaction>) : BalanceUiState()
    data class INCOME(val transactions: List<Transaction>) : BalanceUiState()
    object EDITOR : BalanceUiState()
    object ERROR : BalanceUiState()
}