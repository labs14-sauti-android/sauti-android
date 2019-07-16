package com.labs.sauti.model

import com.google.gson.annotations.SerializedName

class MarketPriceCategory {
    @SerializedName("category")
    var category: String? = null

    constructor()
    constructor(category: String?) {
        this.category = category
    }
}