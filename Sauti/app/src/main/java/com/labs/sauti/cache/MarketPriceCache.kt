package com.labs.sauti.cache

import com.labs.sauti.model.MarketPriceData
import io.reactivex.Single

interface MarketPriceCache {

    fun save(marketPriceData: MarketPriceData)
    fun search(country: String, market: String, category: String, product: String): Single<MarketPriceData>
    fun getAll(): Single<MutableList<MarketPriceData>>
    fun remove(marketPriceData: MarketPriceData)
    fun removeAll()

}