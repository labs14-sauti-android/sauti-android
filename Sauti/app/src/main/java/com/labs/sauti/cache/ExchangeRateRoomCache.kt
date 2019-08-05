package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.ExchangeRateData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ExchangeRateRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : ExchangeRateCache {
    override fun replaceAll(exchangeRates: MutableList<ExchangeRateData>): Completable {
        return sautiRoomDatabase.exchangeRateDao().deleteAll()
            .doOnComplete {
                sautiRoomDatabase.exchangeRateDao().insert(exchangeRates).blockingGet()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun get(currency: String): Single<ExchangeRateData> {
        return sautiRoomDatabase.exchangeRateDao().get(currency)
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(): Single<MutableList<ExchangeRateData>> {
        return sautiRoomDatabase.exchangeRateDao().getAll()
            .subscribeOn(Schedulers.io())
    }

}