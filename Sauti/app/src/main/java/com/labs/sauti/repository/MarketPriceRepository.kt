package com.labs.sauti.repository

import com.labs.sauti.model.market_price.FavoriteMarketPriceSearchData
import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.market_price.MarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single

interface MarketPriceRepository {
    fun updateMarketPrices(): Completable

    fun getMarketPriceCountries(): Single<MutableList<String>>
    fun getMarketPriceMarkets(country: String): Single<MutableList<String>>
    fun getMarketPriceCategories(country: String, market: String): Single<MutableList<String>>
    fun getMarketPriceProducts(country: String, market: String, category: String): Single<MutableList<String>>
    fun searchMarketPrice(country: String, market: String, category: String, product: String): Single<MarketPriceData>

    fun getRecentMarketPriceSearches(): Single<MutableList<MarketPriceSearchData>>
    fun searchRecentMarketPrices(): Single<MutableList<MarketPriceData>>
    fun searchRecentMarketPriceInCache(): Single<MutableList<MarketPriceData>>

    fun syncFavoriteMarketPriceSearches(userId: Long): Completable
    fun isFavorite(userId: Long, country: String, market: String, category: String, product: String): Single<Boolean>
    fun addToFavorite(userId: Long, country: String, market: String, category: String, product: String): Completable
    fun removeFromFavorite(userId: Long, country: String, market: String, category: String, product: String): Completable
    fun getFavoriteMarketPrices(userId: Long): Single<HashMap<MarketPriceData, Long>>
}