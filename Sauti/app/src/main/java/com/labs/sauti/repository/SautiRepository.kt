package com.labs.sauti.repository

import com.labs.sauti.model.*
import io.reactivex.Single

interface SautiRepository {

    fun login(username: String, password: String): Single<LoginResponse>
    fun signOut(): Single<Unit>
    fun isAccessTokenValid(): Single<Boolean>
    fun getCurrentUser(): Single<User>

    fun getMarketPriceCountries(): Single<MutableList<MarketPriceCountry>>
    fun getMarketPriceMarkets(marketPriceCountry: MarketPriceCountry): Single<MutableList<MarketPriceMarket>>
    fun getMarketPriceCategories(
        marketPriceCountry: MarketPriceCountry,
        marketPriceMarket: MarketPriceMarket
    ): Single<MutableList<MarketPriceCategory>>
    fun getMarketPriceCommodities(
        marketPriceCountry: MarketPriceCountry,
        marketPriceMarket: MarketPriceMarket,
        marketPriceCategory: MarketPriceCategory
    ): Single<MutableList<MarketPriceCommodity>>
    fun searchMarketPrice(country: String, market: String, category: String, commodity: String): Single<MarketPrice>
    fun getRecentMarketPrices(): Single<MutableList<MarketPrice>>

}