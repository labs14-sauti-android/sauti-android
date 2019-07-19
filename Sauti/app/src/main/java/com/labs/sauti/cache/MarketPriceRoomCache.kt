package com.labs.sauti.cache

import android.annotation.SuppressLint
import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.MarketPriceData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MarketPriceRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : MarketPriceCache {
    @SuppressLint("CheckResult")
    // TODO proper rxjava
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

    override fun getCountries(): Single<MutableList<String>> {
        return sautiRoomDatabase.marketPriceDao().getCountries()
            .subscribeOn(Schedulers.io())
    }

    override fun getMarkets(country: String): Single<MutableList<String>> {
        return sautiRoomDatabase.marketPriceDao().getMarkets(country)
            .subscribeOn(Schedulers.io())
    }

    override fun getCategories(country: String, market: String): Single<MutableList<String>> {
        return sautiRoomDatabase.marketPriceDao().getCategories(country, market)
            .subscribeOn(Schedulers.io())
    }

    override fun getProducts(country: String, market: String, category: String): Single<MutableList<String>> {
        return sautiRoomDatabase.marketPriceDao().getProducts(country, market, category)
            .subscribeOn(Schedulers.io())
    }

    override fun search(country: String, market: String, category: String, product: String): Single<MarketPriceData> {
        return sautiRoomDatabase.marketPriceDao().getBySearch(country, market, category, product)
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(): Single<MutableList<MarketPriceData>> {
        return sautiRoomDatabase.marketPriceDao().getAll()
            .subscribeOn(Schedulers.io())
    }

    override fun remove(marketPriceData: MarketPriceData): Completable {
        return sautiRoomDatabase.marketPriceDao().delete(marketPriceData)
            .subscribeOn(Schedulers.io())
    }

    override fun removeAll(): Completable {
        return sautiRoomDatabase.marketPriceDao().deleteAll()
            .subscribeOn(Schedulers.io())
    }

}