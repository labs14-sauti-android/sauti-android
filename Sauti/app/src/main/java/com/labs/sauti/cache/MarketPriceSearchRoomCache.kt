package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.MarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MarketPriceSearchRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : MarketPriceSearchCache {

    companion object {
        private const val MAX_ITEMS = 2
    }

    override fun save(recentMarketPriceSearchData: MarketPriceSearchData): Completable {
        return sautiRoomDatabase.marketPriceSearchDao().getCount().flatMapCompletable {
            // remove excess first, so search won't mess up
            run {
                val exceeded = it - MAX_ITEMS
                if (exceeded > 0) {
                    sautiRoomDatabase.marketPriceSearchDao().deleteOldest(exceeded).blockingAwait()
                }
            }

            sautiRoomDatabase.marketPriceSearchDao().getBySearch(
                recentMarketPriceSearchData.country,
                recentMarketPriceSearchData.market,
                recentMarketPriceSearchData.category,
                recentMarketPriceSearchData.product
            ).doOnSuccess {
                recentMarketPriceSearchData.id = it.id
                sautiRoomDatabase.marketPriceSearchDao().update(recentMarketPriceSearchData).blockingAwait()
            }.onErrorResumeNext {
                sautiRoomDatabase.marketPriceSearchDao().insert(recentMarketPriceSearchData).blockingGet()
                val count = sautiRoomDatabase.marketPriceSearchDao().getCount().blockingGet()
                val exceeded = count - MAX_ITEMS
                if (exceeded > 0) {
                    sautiRoomDatabase.marketPriceSearchDao().deleteOldest(exceeded).blockingAwait()
                }

                Single.just(recentMarketPriceSearchData)
            }.flatMapCompletable {
                Completable.complete()
            }
        }
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(): Single<MutableList<MarketPriceSearchData>> {
        return sautiRoomDatabase.marketPriceSearchDao().getAllByIdDesc()
            .subscribeOn(Schedulers.io())
    }
}