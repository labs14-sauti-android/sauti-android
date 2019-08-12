package com.labs.sauti.repository

import com.labs.sauti.model.*
import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.market_price.MarketPriceSearchData
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResultData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import io.reactivex.Completable
import io.reactivex.Single

interface SautiRepository {

    fun signUp(signUpRequest: SignUpRequest): Single<SignUpResponse>
    fun signIn(username: String, password: String): Single<SignInResponse>
    fun signOut(): Completable
    fun isAccessTokenValid(): Single<Boolean>
    fun getCurrentUser(): Single<User>

    fun getMarketPriceCountries(): Single<MutableList<String>>
    fun getMarketPriceMarkets(country: String): Single<MutableList<String>>
    fun getMarketPriceCategories(country: String, market: String): Single<MutableList<String>>
    fun getMarketPriceProducts(country: String, market: String, category: String): Single<MutableList<String>>
    fun searchMarketPrice(country: String, market: String, category: String, product: String): Single<MarketPriceData>

    fun getRecentMarketPriceSearches(): Single<MutableList<MarketPriceSearchData>>
    fun searchRecentMarketPrices(): Single<MutableList<MarketPriceData>>
    fun searchRecentMarketPriceInCache(): Single<MutableList<MarketPriceData>>

    fun getExchangeRates(): Single<MutableList<ExchangeRateData>>
    fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double): Single<ExchangeRateConversionResultData>
    fun getRecentConversionResults(): Single<MutableList<ExchangeRateConversionResultData>>
    fun getRecentConversionResultsInCache(): Single<MutableList<ExchangeRateConversionResultData>>

    fun getSelectedLanguage(): Single<String>
    fun setSelectedLanguage(language: String): Completable

    fun getTradeInfoProductCategory(language: String): Single<MutableList<String>>

    fun getTradeInfoBorderAgenciesCountries()

}