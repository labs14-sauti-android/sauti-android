package com.labs.sauti.cache

import io.reactivex.Single

interface TradeInfoCache {

    fun getCountries(language: String): Single<MutableList<String>>
}