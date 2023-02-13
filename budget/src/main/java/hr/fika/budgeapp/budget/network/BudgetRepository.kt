package hr.fika.budgeapp.budget.network

import hr.fika.budgeapp.budget.model.Budget
import hr.fika.budgeapp.common.bank.model.BudgetCalculationInfo

object BudgetRepository {

    suspend fun getCalculationInfo(accountId: Int) : BudgetCalculationInfo? {
        return BudgetApiProvider.provideBudgetApi().getCalculation(accountId).body()
    }

    suspend fun addBudget(budget: Budget) : String? {
        return BudgetApiProvider.provideBudgetApi().addBudget(budget).body()
    }

    suspend fun getBudgets(accountId: Int, userId: Int) : List<Budget>? {
        return BudgetApiProvider.provideBudgetApi().getBudgets(accountId, userId).body()
    }
}