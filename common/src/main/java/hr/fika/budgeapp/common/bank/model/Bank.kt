package hr.fika.budgeapp.common.bank.model

import com.google.gson.annotations.SerializedName

data class Bank (
    @SerializedName("idBank") val idBank: Int,
    @SerializedName("name") val name: String,
)