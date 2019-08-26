package com.labs.sauti.model.market_price

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "favorite_market_price_searches")
data class FavoriteMarketPriceSearchData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @Expose(serialize = false)
    var userId: Long? = null,

    @Expose
    var favoriteMarketPriceSearchId: Long? = null,

    @Expose
    var country: String? = null,

    @Expose
    var market: String? = null,

    @Expose
    var category: String? = null,

    @Expose
    var product: String? = null,

    @Expose
    var timestamp: Long? = null,

    var shouldRemove: Int
)