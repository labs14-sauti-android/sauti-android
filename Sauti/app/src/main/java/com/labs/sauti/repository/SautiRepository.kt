package com.labs.sauti.repository

import com.labs.sauti.cache.RecentMarketPriceCache
import com.labs.sauti.model.*
import io.reactivex.Single

interface SautiRepository {

    fun login(username: String, password: String): Single<LoginResponse>
    fun signOut(): Single<Unit>
    fun isAccessTokenValid(): Single<Boolean>
    fun getCurrentUser(): Single<User>

    fun getMarketPriceCountries(): Single<MutableList<String>>
    fun getMarketPriceMarkets(country: String): Single<MutableList<String>>
    fun getMarketPriceCategories(country: String, market: String): Single<MutableList<String>>
    fun getMarketPriceProducts(country: String, market: String, category: String): Single<MutableList<String>>
    fun searchMarketPrice(country: String, market: String, category: String, product: String): Single<MarketPriceData>

    fun getRecentMarketPrices(): Single<MutableList<RecentMarketPriceData>>

    fun getRecentMarketPriceSearches(): Single<MutableList<RecentMarketPriceSearchData>>
    fun searchRecentMarketPrices(): Single<MutableList<MarketPriceData>>
    fun searchRecentMarketPriceInCache(): Single<MutableList<MarketPriceData>>

}