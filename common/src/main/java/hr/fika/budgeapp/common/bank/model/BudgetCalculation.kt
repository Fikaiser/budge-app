package hr.fika.budgeapp.common.bank.model

import com.google.gson.annotations.SerializedName

data class BudgetCalculationInfo (
    @SerializedName("flow") val flow: List<Transaction>,
    @SerializedName("total") val total: Double,
    @SerializedName("netChange") val netChange: Double
)