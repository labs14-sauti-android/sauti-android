package com.labs.sauti.view_state.market_price

import com.labs.sauti.model.market_price.MarketPrice

class RecentMarketPricesViewState(
    var isLoading: Boolean = true,
    var recentMarketPrices: List<MarketPrice>? = null
)