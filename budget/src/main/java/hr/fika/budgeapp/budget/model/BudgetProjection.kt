package hr.fika.budgeapp.budget.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class BudgetProjection (
    @SerializedName("budget") val budget: Budget,
    @SerializedName("change") val change: Double,
    @SerializedName("isOnTarget") val isOnTarget: Boolean
) {
    fun getDate() : String {
        val date = Instant
            .ofEpochMilli(budget.budgetDate!!)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return date.format(formatter)
    }
}