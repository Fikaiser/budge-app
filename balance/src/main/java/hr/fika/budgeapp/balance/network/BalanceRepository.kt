package hr.fika.budgeapp.balance.network

import hr.fika.budgeapp.common.bank.model.BankAccount


object BalanceRepository {

    suspend fun getGreet() : String? {
        return BalanceApiProvider.provideBalanceApi().getGreet().body()
    }

    suspend fun linkBankAccount(userId: Int, bankId: Int) : BankAccount? {
        return BalanceApiProvider.provideBalanceApi().linkBankAccount(userId, bankId).body()
    }
}

class BalanceRepo() {

    suspend fun getGreet() : String? {
        return BalanceApiProvider.provideBalanceApi().getGreet().body()
    }

    suspend fun linkBankAccount(userId: Int, bankId: Int) : BankAccount? {
        return BalanceApiProvider.provideBalanceApi().linkBankAccount(userId, bankId).body()
    }
}