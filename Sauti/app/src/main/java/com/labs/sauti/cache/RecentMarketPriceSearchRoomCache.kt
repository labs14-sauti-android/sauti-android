package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.RecentMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

// TODO observeon instead of subscribeon
class RecentMarketPriceSearchRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : RecentMarketPriceSearchCache {

    companion object {
        private const val MAX_ITEMS = 2
    }

    override fun save(recentMarketPriceSearchData: RecentMarketPriceSearchData): Completable {
        return sautiRoomDatabase.recentMarketPriceSearchDao().getCount().flatMapCompletable {
            // remove excess first, so search won't mess up
            run {
                val exceeded = it - MAX_ITEMS
                if (exceeded > 0) {
                    sautiRoomDatabase.recentMarketPriceSearchDao().deleteOldest(exceeded).blockingAwait()
                }
            }

            sautiRoomDatabase.recentMarketPriceSearchDao().getBySearch(
                recentMarketPriceSearchData.country,
                recentMarketPriceSearchData.market,
                recentMarketPriceSearchData.category,
                recentMarketPriceSearchData.product
            ).doOnSuccess {
                recentMarketPriceSearchData.id = it.id
                sautiRoomDatabase.recentMarketPriceSearchDao().update(recentMarketPriceSearchData).blockingAwait()
            }.onErrorResumeNext {
                sautiRoomDatabase.recentMarketPriceSearchDao().insert(recentMarketPriceSearchData).blockingGet()
                val count = sautiRoomDatabase.recentMarketPriceSearchDao().getCount().blockingGet()
                val exceeded = count - MAX_ITEMS
                if (exceeded > 0) {
                    sautiRoomDatabase.recentMarketPriceSearchDao().deleteOldest(exceeded).blockingAwait()
                }

                Single.just(recentMarketPriceSearchData)
            }.flatMapCompletable {
                Completable.complete()
            }
        }
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(): Single<MutableList<RecentMarketPriceSearchData>> {
        return sautiRoomDatabase.recentMarketPriceSearchDao().getAllByIdDesc()
            .subscribeOn(Schedulers.io())
    }
}