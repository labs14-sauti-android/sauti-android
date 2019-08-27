package com.labs.sauti.cache

import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import io.reactivex.Single

interface TradeInfoCache {

    fun getTIProductCategories  (language: String) : Single<MutableList<String>>
    fun getTIProductProducts    (language: String, category: String) : Single<MutableList<String>>
    fun getTIProductOrigin      (language: String, category: String, product: String) : Single<MutableList<String>>
    fun getTIDests              (language: String, category: String, product: String, origin: String) : Single<MutableList<String>>

    fun searchTradeInfoBorderProcedures(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfo>
    fun searchTradeInfoRequiredDocuments(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfo>
    fun searchTradeInfoBorderAgencies(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfo>

    //TODO: This needs extra stuff
    fun searchTradeInfoTaxCalculator(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfoTaxes>

    fun getTIRegulatedGoodsCountries    (language: String) : Single<MutableList<String>>
    fun getTIRegulatedGoodsProhibited   (language: String, country: String) : Single<TradeInfo>
    fun getTIRegulatedGoodsRestricted   (language: String, country: String) : Single<TradeInfo>
    fun getTIRegulatedGoodsSensitive    (language: String, country: String) : Single<TradeInfo>


}