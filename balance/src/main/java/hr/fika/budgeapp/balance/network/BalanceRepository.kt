package hr.fika.budgeapp.balance.network

import hr.fika.budgeapp.common.bank.model.BankAccount
import hr.fika.budgeapp.common.bank.model.Transaction


object BalanceRepository {

    suspend fun getTransactions(accountId: Int) : List<Transaction>? {
        return BalanceApiProvider.provideBalanceApi().getTransactions(accountId).body()
    }

    suspend fun saveTransaction(transaction: Transaction) {
        BalanceApiProvider.provideBalanceApi().saveTransaction(transaction)
    }

    suspend fun deleteTransaction(transactionId: Int) {
        BalanceApiProvider.provideBalanceApi().deleteTransaction(transactionId)
    }

    suspend fun getFlow(accountId: Int) : List<Transaction>? {
        return BalanceApiProvider.provideBalanceApi().getFlow(accountId).body()
    }

    suspend fun linkBankAccount(userId: Int, bankId: Int) : BankAccount? {
        return BalanceApiProvider.provideBalanceApi().linkBankAccount(userId, bankId).body()
    }
}
