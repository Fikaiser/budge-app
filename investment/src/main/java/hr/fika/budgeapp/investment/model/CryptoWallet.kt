package hr.fika.budgeapp.investment.model

import com.google.gson.annotations.SerializedName

data class CryptoWallet (
    @SerializedName("idCryptoWallet") val idWallet: Int? = null,
    @SerializedName("userId")val userId: Int? = null
)