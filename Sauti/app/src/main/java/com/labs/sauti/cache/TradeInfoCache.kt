package com.labs.sauti.cache

import com.labs.sauti.model.trade_info.*
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface TradeInfoCache {

    fun getTwoRecentTaxCalcModels   (): Single<MutableList<TradeInfoData>>

    fun getTwoRecentTradeInfoModels ():Single<MutableList<TradeInfoData>>

    fun getRegulatedCountries       (language: String) : Single<MutableList<String>>

    fun saveRegulatedProhibiteds    (prohibited: TradeInfoData): Completable
    fun searchRegulatedProhibiteds  (language: String,
                                     regulatedCountry: String): Single<TradeInfoData>

    fun saveRegulatedRestricteds    (restricted: TradeInfoData): Completable
    fun searchRegulatedRestricteds  (language: String,
                                     regulatedCountry: String): Single<TradeInfoData>

    fun saveRegulatedSensitives     (sensitive: TradeInfoData): Completable
    fun searchRegulatedSensitives   (language: String,
                                     regulatedCountry: String): Single<TradeInfoData>

    //Search Terms for Procedures, Documents, Agencies
    fun getTIProductCategories      (language: String) : Single<MutableList<String>>
    fun getTIProductProducts        (language: String,
                                     category: String) : Single<MutableList<String>>
    fun getTIProductOrigin          (language: String,
                                     category: String,
                                     product: String) : Single<MutableList<String>>
    fun getTIDests                  (language: String,
                                     category: String,
                                     product: String,
                                     origin: String) : Single<MutableList<String>>

    fun getTIUserCurrency           (language: String,
                                     category: String,
                                     product: String,
                                     origin: String,
                                     dest: String) : Single<MutableList<String>>


    fun saveTIProcedures            (borderProcedure: TradeInfoData):Completable
    fun searchTIProcedures          (language: String,
                                     category: String,
                                     product: String,
                                     origin: String,
                                     dest: String,
                                     value: Double): Single<TradeInfoData>

    fun saveTIDocuments             (requiredDocument: TradeInfoData): Completable
    fun searchTIDocuments           (language: String,
                                     category: String,
                                     product: String,
                                     origin: String,
                                     dest: String,
                                     value: Double): Single<TradeInfoData>

    fun saveTIAgencies              (agencies: TradeInfoData): Completable
    fun searchTIAgencies            (language: String,
                                     category: String,
                                     product: String,
                                     origin: String,
                                     dest: String,
                                     value: Double): Single<TradeInfoData>

    fun saveTITaxes                 (taxes: TradeInfoData): Completable
    fun searchTITaxes               (language: String,
                                     category: String,
                                     product: String,
                                     origin: String,
                                     dest: String,
                                     value: Double,
                                     userCurrency: String,
                                     destCurrency: String): Single<TradeInfoData>

//    fun searchTradeInfoRequiredDocuments(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfo>
//    fun searchTradeInfoBorderAgencies(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfo>

    //TODO: This needs extra stuff
//    fun searchTradeInfoTaxCalculator(language: String, category: String, product: String, origin: String, dest: String, value: Double): Single<TradeInfoTaxes>

//    fun getTIRegulatedGoodsCountries    (language: String) : Single<MutableList<String>>
//    fun getTIRegulatedGoodsProhibited   (language: String, country: String) : Single<TradeInfoData>
//    fun getTIRegulatedGoodsRestricted   (language: String, country: String) : Single<TradeInfo>
//    fun getTIRegulatedGoodsSensitive    (language: String, country: String) : Single<TradeInfo>


}