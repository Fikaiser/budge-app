package hr.fika.budgeapp.investment.model

import com.google.gson.annotations.SerializedName
import com.himanshoe.charty.line.model.LineData

data class HistoricalCoinPrice(
    @SerializedName("change") var change: Double? = null,
    @SerializedName("history") var history: List<CoinPriceHistory> = listOf()
)

data class CoinPriceHistory(
    @SerializedName("price") var price: String? = null,
    @SerializedName("timestamp") var timestamp: Int? = null
) {
    fun toLineData() = LineData(timestamp!!.toFloat(), price!!.toFloat())
}