package com.labs.sauti.mapper

import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.model.market_price.MarketPriceData

class MarketPriceDataToMarketPriceMapper : Mapper<MarketPriceData, MarketPrice>() {
    override fun mapFrom(from: MarketPriceData): MarketPrice {
        return MarketPrice(
            from.country,
            from.market,
            from.productAgg,
            from.productCat,
            from.product,
            from.wholesale,
            from.retail,
            from.currency,
            from.date,
            from.nearbyMarketplaceNames
        )
    }
}