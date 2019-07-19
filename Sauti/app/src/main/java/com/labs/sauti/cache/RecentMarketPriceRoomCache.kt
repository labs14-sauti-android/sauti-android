package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.RecentMarketPriceData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RecentMarketPriceRoomCache(
    private val sautiRoomDatabase: SautiRoomDatabase
) : RecentMarketPriceCache {

    companion object {
        private const val MAX_RECENT_ITEMS = 20
    }

    override fun save(recentMarketPriceData: RecentMarketPriceData): Completable {
        return sautiRoomDatabase.recentMarketPriceDao().insert(recentMarketPriceData)
            .andThen(sautiRoomDatabase.recentMarketPriceDao().getCount())
            .flatMapCompletable {
                val exceededBy = it - MAX_RECENT_ITEMS
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