package com.labs.sauti.cache

import com.labs.sauti.model.market_price.MarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single

// TODO use entity because this is in the domain layer
interface MarketPriceSearchCache {

    fun save(recentMarketPriceSearchData: MarketPriceSearchData): Completable
    fun getAll(): Single<MutableList<MarketPriceSearchData>>
}