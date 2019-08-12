package com.labs.sauti.cache

import io.reactivex.Single

interface TradeInfoCache {

    fun getTIProductCategories(language: String): Single<MutableList<String>>
    fun getRegulatedGoodsCountries(language: String) : Single<MutableList<String>>

}