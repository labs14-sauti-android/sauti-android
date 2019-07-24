package com.labs.sauti.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_market_price_searches")
data class RecentMarketPriceSearchData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var country: String,
    var market: String,
    var category: String,
    var product: String
)