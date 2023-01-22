package hr.fika.budgeapp.common.bank.model

import com.google.gson.annotations.SerializedName

data class BankAccount (
    @SerializedName("idBankAccount") val idBankAccount: Int? = null,
    @SerializedName("bankId")val bankId: Int? = null
)