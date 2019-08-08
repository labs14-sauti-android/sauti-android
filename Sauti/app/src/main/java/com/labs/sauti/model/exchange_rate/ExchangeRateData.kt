package com.labs.sauti.model.exchange_rate

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exchange_rates")
data class ExchangeRateData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @SerializedName("currency")
    @Expose
    var currency: String? = null,

    @SerializedName("rate")
    @Expose
    var rate: Double? = null

)