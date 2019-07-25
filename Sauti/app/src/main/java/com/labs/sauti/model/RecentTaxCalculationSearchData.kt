package com.labs.sauti.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_market_price_searches")
data class RecentTaxCalculationSearchData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var category: String,
    var product: String,
    var whereTo: String,
    var whereFrom: String,
    var value: Long
)