package hr.fika.budgeapp.investment.model

import com.google.gson.annotations.SerializedName

data class StockPortfolio (
    @SerializedName("idStockPortfolio") val idStockPortfolio: Int? = null,
    @SerializedName("userId")val userId: Int? = null
)