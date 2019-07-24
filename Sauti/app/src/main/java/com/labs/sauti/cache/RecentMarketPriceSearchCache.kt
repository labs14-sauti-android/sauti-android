package com.labs.sauti.cache

import com.labs.sauti.model.RecentMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single

// TODO use entity because this is in the domain layer
interface RecentMarketPriceSearchCache {

    fun save(recentMarketPriceSearchData: RecentMarketPriceSearchData): Completable
    fun getAll(): Single<MutableList<RecentMarketPriceSearchData>>
}