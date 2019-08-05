package com.labs.sauti.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var currency: String,
    var rate: Double

)