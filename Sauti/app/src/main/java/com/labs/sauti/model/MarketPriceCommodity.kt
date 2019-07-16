package com.labs.sauti.model

import com.google.gson.annotations.SerializedName

class MarketPriceCommodity {
    @SerializedName("commodity")
    var commodity: String? = null

    constructor()
    constructor(commodity: String?) {
        this.commodity = commodity
    }
}