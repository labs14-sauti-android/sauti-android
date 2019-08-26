package com.labs.sauti.repository

import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResultData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import io.reactivex.Completable
import io.reactivex.Single

interface ExchangeRateRepository {
    fun getExchangeRates(): Single<MutableList<ExchangeRateData>>
    fun convertCurrency(
        shouldSaveConversion: Boolean,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Single<ExchangeRateConversionResultData>
    fun getRecentConversionResults(): Single<MutableList<ExchangeRateConversionResultData>>
    fun getRecentConversionResultsInCache(): Single<MutableList<ExchangeRateConversionResultData>>

    fun syncFavoriteExchangeRateConversions(userId: Long): Completable
    fun isFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Single<Boolean>
    fun addToFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Completable
    fun removeFromFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Completable
    fun getFavoriteExchangeRateConversionResults(userId: Long): Single<HashMap<ExchangeRateConversionResultData, Long>>
}