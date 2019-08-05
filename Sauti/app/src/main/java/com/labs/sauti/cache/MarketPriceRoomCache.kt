package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.market_price.MarketPriceData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MarketPriceRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : MarketPriceCache {
    override fun save(marketPriceData: MarketPriceData): Completable {
        return search(
            marketPriceData.country!!,
            marketPriceData.market!!,
            marketPriceData.productCat!!,
            marketPriceData.product!!
        ).doOnSuccess {
            marketPriceData.id = it.id
            sautiRoomDatabase.marketPriceDao().update(marketPriceData).blockingAwait()
        }.onErrorResumeNext {
            sautiRoomDatabase.marketPriceDao().insert(marketPriceData).flatMapSingle { Single.just(marketPriceData) }
        }.flatMapCompletable {
            Completable.complete()
        }
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