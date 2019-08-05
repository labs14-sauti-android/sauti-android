package com.labs.sauti.model.market_price

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market_price_searches")
data class MarketPriceSearchData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var country: String,
    var market: String,
    var category: String,
    var product: String
)