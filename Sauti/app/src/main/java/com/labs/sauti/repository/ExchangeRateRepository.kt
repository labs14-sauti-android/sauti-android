package com.labs.sauti.repository

import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResultData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import io.reactivex.Single

interface ExchangeRateRepository {
    fun getExchangeRates(): Single<MutableList<ExchangeRateData>>
    fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double): Single<ExchangeRateConversionResultData>
    fun getRecentConversionResults(): Single<MutableList<ExchangeRateConversionResultData>>
    fun getRecentConversionResultsInCache(): Single<MutableList<ExchangeRateConversionResultData>>
}