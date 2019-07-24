package com.labs.sauti.cache

import com.labs.sauti.model.RecentMarketPriceData
import io.reactivex.Completable
import io.reactivex.Single

// TODO remove RecentMarketPrice
interface RecentMarketPriceCache {

    fun save(recentMarketPriceData: RecentMarketPriceData): Completable
    fun getAll(): Single<MutableList<RecentMarketPriceData>>

}