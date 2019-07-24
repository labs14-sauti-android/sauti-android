package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.RecentMarketPriceData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

// TODO remove RecentMarketPrice
class RecentMarketPriceRoomCache(
    private val sautiRoomDatabase: SautiRoomDatabase
) : RecentMarketPriceCache {

    companion object {
        private const val MAX_RECENT_ITEMS = 20
    }

    override fun save(recentMarketPriceData: RecentMarketPriceData): Completable {
        return sautiRoomDatabase.recentMarketPriceDao().insert(recentMarketPriceData)
            .flatMapCompletable {
                val count = sautiRoomDatabase.recentMarketPriceDao().getCount().blockingGet()
                val exceededBy = count - MAX_RECENT_ITEMS
                if (exceededBy > 0) {
                    return@flatMapCompletable sautiRoomDatabase.recentMarketPriceDao().deleteOldestRecentMarketPrice(exceededBy)
                }

                Completable.complete()
            }
    }

    override fun getAll(): Single<MutableList<RecentMarketPriceData>> {
        return sautiRoomDatabase.recentMarketPriceDao().getAllRecentMarketPriceOrderByTimeCreated()
            .subscribeOn(Schedulers.io())
    }

}