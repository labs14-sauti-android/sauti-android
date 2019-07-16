package com.labs.sauti.model

import com.google.gson.annotations.SerializedName

class MarketPriceCountry {

    @SerializedName("country")
    var country: String? = null

    constructor()
    constructor(country: String?) {
        this.country = country
    }
}