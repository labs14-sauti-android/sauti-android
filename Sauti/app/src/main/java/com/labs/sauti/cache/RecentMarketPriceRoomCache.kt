package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.RecentMarketPriceData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RecentMarketPriceRoomCache(
    private val sautiRoomDatabase: SautiRoomDatabase
) : RecentMarketPriceCache {

    companion object {
        private const val MAX_CACHE_SIZE = 5 // TODO test only, use 1000 or more
    }

    override fun save(recentMarketPriceData: RecentMarketPriceData) {
        sautiRoomDatabase.recentMarketPriceDao().insert(recentMarketPriceData)
        val exceededBy = sautiRoomDatabase.recentMarketPriceDao().getCount() - MAX_CACHE_SIZE
        if (exceededBy > 0) {
            sautiRoomDatabase.recentMarketPriceDao().deleteOldestRecentMarketPrice(exceededBy)
        }
    }

    override fun getAll(): Single<MutableList<RecentMarketPriceData>> {
        return Single.fromCallable {
            sautiRoomDatabase.recentMarketPriceDao().getAllRecentMarketPriceOrderByTimeCreated()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    }

}