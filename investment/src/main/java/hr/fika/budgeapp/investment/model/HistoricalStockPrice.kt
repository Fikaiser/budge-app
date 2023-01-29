package hr.fika.budgeapp.investment.model

import com.google.gson.annotations.SerializedName

data class HistoricalStockPrice(
    @SerializedName("values") var values: List<HistoricalStockValues> = listOf(),
)

data class HistoricalStockValues(
    @SerializedName("datetime") var datetime: String? = null,
    @SerializedName("open") var open: String? = null,
    @SerializedName("high") var high: String? = null,
    @SerializedName("low") var low: String? = null,
    @SerializedName("close") var close: String? = null,
    @SerializedName("volume") var volume: String? = null
)