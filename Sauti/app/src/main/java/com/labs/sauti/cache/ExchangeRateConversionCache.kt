package com.labs.sauti.cache

import com.labs.sauti.model.exchange_rate.ExchangeRateConversionData
import io.reactivex.Completable
import io.reactivex.Single

interface ExchangeRateConversionCache {
    fun save(exchangeRateConversionData: ExchangeRateConversionData): Completable
    fun getAll(): Single<MutableList<ExchangeRateConversionData>>
}