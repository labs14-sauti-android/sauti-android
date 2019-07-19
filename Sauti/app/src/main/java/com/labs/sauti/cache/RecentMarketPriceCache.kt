package com.labs.sauti.cache

import com.labs.sauti.model.RecentMarketPriceData
import io.reactivex.Single

// TODO use entity because this is in the domain layer
interface RecentMarketPriceCache {

    fun save(recentMarketPriceData: RecentMarketPriceData)
    fun getAll(): Single<MutableList<RecentMarketPriceData>>

}