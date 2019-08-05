package com.labs.sauti.model.exchange_rate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate_conversions")
class ExchangeRateConversionData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var fromCurrency: String,
    var toCurrency: String,
    var amount: Double
)