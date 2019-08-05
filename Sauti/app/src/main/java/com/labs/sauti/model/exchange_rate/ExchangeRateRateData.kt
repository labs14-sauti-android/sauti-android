package com.labs.sauti.model.exchange_rate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExchangeRateRateData(
    @SerializedName("rate")
    @Expose
    var rate: Double? = null
)