package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.ExchangeRateConversionData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ExchangeRateConversionRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : ExchangeRateConversionCache {

    companion object {
        private const val MAX_ITEMS = 2
    }

    override fun save(exchangeRateConversionData: ExchangeRateConversionData): Completable {
        return sautiRoomDatabase.exchangeRateConversionDao().insert(exchangeRateConversionData)
            .flatMapSingle {
                sautiRoomDatabase.exchangeRateConversionDao().getCount()
            }
            .flatMapCompletable {
                val exceed = it - MAX_ITEMS
                if (exceed > 0) {
                    return@flatMapCompletable sautiRoomDatabase.exchangeRateConversionDao().deleteOldest(exceed)
                }

                Completable.complete()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(): Single<MutableList<ExchangeRateConversionData>> {
        return sautiRoomDatabase.exchangeRateConversionDao().getAllOrderByIdDesc()
            .subscribeOn(Schedulers.io())
    }

}