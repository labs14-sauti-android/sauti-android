package com.labs.sauti.mapper

import com.labs.sauti.model.MarketPriceData
import com.labs.sauti.model.RecentMarketPriceData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketPriceDataRecentMarketPriceDataMapper @Inject constructor() : Mapper<MarketPriceData, RecentMarketPriceData>() {

    override fun mapFrom(from: MarketPriceData) =
        RecentMarketPriceData(
            id = from.id,
            country = from.country,
            market = from.market,
            productAgg = from.productAgg,
            productCat = from.productCat,
            product = from.product,
            wholesale = from.wholesale,
            retail = from.retail,
            currency = from.currency,
            date = from.date
        )

}