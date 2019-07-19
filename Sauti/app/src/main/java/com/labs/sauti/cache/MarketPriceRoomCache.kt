package com.labs.sauti.cache

import android.annotation.SuppressLint
import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.MarketPriceData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MarketPriceRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : MarketPriceCache {
    @SuppressLint("CheckResult")
    override fun save(marketPriceData: MarketPriceData) {
        search(
            marketPriceData.country!!,
            marketPriceData.market!!,
            marketPriceData.productCat!!,
            marketPriceData.product!!
        ).subscribe(
            {
                marketPriceData.id = it.id
                sautiRoomDatabase.marketPriceDao().update(marketPriceData)
            },
            {
                sautiRoomDatabase.marketPriceDao().insert(marketPriceData)
            }
        )
    }

    override fun search(country: String, market: String, category: String, product: String): Single<MarketPriceData> {
        return Single.fromCallable {
            sautiRoomDatabase.marketPriceDao().getBySearch(country, market, category, product) ?: throw Throwable()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    override fun getAll(): Single<MutableList<MarketPriceData>> {
        return Single.fromCallable {
            sautiRoomDatabase.marketPriceDao().getAll()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    override fun remove(marketPriceData: MarketPriceData) {
        sautiRoomDatabase.marketPriceDao().delete(marketPriceData)
    }

    override fun removeAll() {
        sautiRoomDatabase.marketPriceDao().deleteAll()
    }

}