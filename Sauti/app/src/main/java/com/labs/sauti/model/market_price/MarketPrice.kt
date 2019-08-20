package com.labs.sauti.model.market_price

class MarketPrice(
    var country: String? = null,
    var market: String? = null,
    var productAgg: String? = null,
    var productCat: String? = null,
    var product: String? = null,
    var wholesale: Double? = null,
    var retail: Double? = null,
    var currency: String? = null,
    var date: String? = null,
    var nearbyMarketplaceNames: MutableList<String> = mutableListOf()
)