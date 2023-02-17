package hr.fika.budgeapp.investment.model

import com.google.gson.annotations.SerializedName
import com.himanshoe.charty.line.model.LineData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
) {
    fun toLineData() : LineData {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(datetime!!, formatter)
        return LineData(date.dayOfMonth.toFloat(), close!!.toFloat())
    }
}