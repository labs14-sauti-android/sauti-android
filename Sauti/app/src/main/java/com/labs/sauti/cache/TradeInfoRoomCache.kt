package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TradeInfoRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : TradeInfoCache {

    override fun getCountries(language: String): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoCategories(language)
            .subscribeOn(Schedulers.io())
    }


}