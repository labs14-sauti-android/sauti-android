package com.labs.sauti.model

import com.google.gson.annotations.SerializedName

class MarketPriceMarket {
    @SerializedName("market")
    var market: String? = null

    constructor()
    constructor(market: String?) {
        this.market = market
    }
}