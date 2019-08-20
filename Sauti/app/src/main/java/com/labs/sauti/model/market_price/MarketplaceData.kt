package com.labs.sauti.model.market_price

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MarketplaceData(
    @SerializedName("marketplace_name")
    @Expose
    var name: String? = null,

    @SerializedName("marketplace_country")
    @Expose
    var country: String? = null,

    @SerializedName("coord_lat")
    @Expose
    var lat: String? = null,

    @SerializedName("coord_long")
    @Expose
    var lon: String? = null
)