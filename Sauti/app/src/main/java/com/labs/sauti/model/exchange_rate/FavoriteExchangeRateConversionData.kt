package com.labs.sauti.model.exchange_rate

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "favorite_exchange_rate_conversions")
data class FavoriteExchangeRateConversionData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @Expose(serialize = false)
    var userId: Long? = null,

    @Expose
    var favoriteExchangeRateConversionId: Long? = null,

    @Expose
    var fromCurrency: String? = null,

    @Expose
    var toCurrency: String? = null,

    @Expose
    var amount: Double? = null,

    var shouldRemove: Int
)