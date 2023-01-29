package hr.fika.budgeapp.common.user.model

import com.google.gson.annotations.SerializedName
import hr.fika.budgeapp.common.bank.model.BankAccount

data class User (
    @SerializedName("idUser") val idUser: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("token") val apiToken: String,
    @SerializedName("bankAccount") var bankAccount: BankAccount? = null,
    @SerializedName("wallet") var cryptoWalletId: Int? = null,
    @SerializedName("portfolio") var stockPortfolioId: Int? = null,
)