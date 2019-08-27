package com.labs.sauti.cache

import com.labs.sauti.model.trade_info.Procedure
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoData
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import io.reactivex.Completable
import io.reactivex.Single

interface TradeInfoCache {

    fun getTIProductCategories  (language: String) : Single<MutableList<String>>
    fun getTIProductProducts    (language: String, category: String) : Single<MutableList<String>>
    fun getTIProductOrigin      (language: String, category: String, product: String) : Single<MutableList<String>>
    fun getTIDests              (language: String, category: String, product: String, origin: String) : Single<MutableList<String>>

    fun saveTIProcedures(borderProcedure: TradeInfoData):Completable

    fun searchTIProcedures(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfoData>


//    fun searchTradeInfoRequiredDocuments(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfo>
//    fun searchTradeInfoBorderAgencies(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfo>

    //TODO: This needs extra stuff
//    fun searchTradeInfoTaxCalculator(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfoTaxes>

//    fun getTIRegulatedGoodsCountries    (language: String) : Single<MutableList<String>>
//    fun getTIRegulatedGoodsProhibited   (language: String, country: String) : Single<TradeInfoData>
//    fun getTIRegulatedGoodsRestricted   (language: String, country: String) : Single<TradeInfo>
//    fun getTIRegulatedGoodsSensitive    (language: String, country: String) : Single<TradeInfo>


}