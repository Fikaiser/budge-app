package hr.fika.budgeapp.budget.network

import hr.fika.budgeapp.budget.model.Budget
import hr.fika.budgeapp.budget.model.BudgetProjection
import hr.fika.budgeapp.common.bank.model.BudgetCalculationInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BudgetApi {
    @GET("/budgetcalculation")
    suspend fun getCalculation(
        @Query("accountId") bankAccountId: Int
    ): Response<BudgetCalculationInfo>

    @POST("/budgets")
    suspend fun addBudget(
        @Body budget: Budget
    ): Response<String>

    @DELETE("/budgets")
    suspend fun deleteBudget(
        @Query("budgetId") budgetId: Int
    ): Response<String>

    @GET("/budgets")
    suspend fun getBudgets(
        @Query("accountId") bankAccountId: Int,
        @Query("userId") userId: Int
    ): Response<List<BudgetProjection>>
}