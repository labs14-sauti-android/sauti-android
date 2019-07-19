package com.labs.sauti.cache

import com.labs.sauti.model.MarketPriceData
import io.reactivex.Completable
import io.reactivex.Single

interface MarketPriceCache {

    fun save(marketPriceData: MarketPriceData): Completable
    fun getCountries(): Single<MutableList<String>>
    fun getMarkets(country: String): Single<MutableList<String>>
    fun getCategories(country: String, market: String): Single<MutableList<String>>
    fun getProducts(country: String, market: String, category: String): Single<MutableList<String>>
    fun search(country: String, market: String, category: String, product: String): Single<MarketPriceData>
    fun getAll(): Single<MutableList<MarketPriceData>>
    fun remove(marketPriceData: MarketPriceData): Completable
    fun removeAll(): Completable

}