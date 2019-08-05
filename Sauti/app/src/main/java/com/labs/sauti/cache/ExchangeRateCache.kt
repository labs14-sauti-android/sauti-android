package com.labs.sauti.cache

import com.labs.sauti.model.exchange_rate.ExchangeRateData
import io.reactivex.Completable
import io.reactivex.Single

interface ExchangeRateCache {
    fun replaceAll(exchangeRates: MutableList<ExchangeRateData>): Completable
    fun get(currency: String): Single<ExchangeRateData>
    fun getAll(): Single<MutableList<ExchangeRateData>>

}