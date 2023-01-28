package hr.fika.budgeapp.common.bank.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Transaction (
    @SerializedName("idTransaction") val id: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("transactionTimestamp") val transactionTimestamp: Long? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("reoccurring") val reoccurring: Boolean? = null,
    @SerializedName("accountId") val accountId: Int? = null
) {
    override fun toString() = "ID - $id Desc - $description Date- ${Instant.ofEpochMilli(transactionTimestamp!!)} Amount- $amount Account- $accountId"
}