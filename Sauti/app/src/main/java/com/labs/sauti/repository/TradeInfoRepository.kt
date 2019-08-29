package com.labs.sauti.repository

import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.trade_info.*
import io.reactivex.Completable
import io.reactivex.Single

interface TradeInfoRepository {

    fun getSelectedLanguage(): Single<String>
    fun setSelectedLanguage(language: String): Completable

    fun getTradeInfoProductCategory (language: String): Single<MutableList<String>>
    fun getTradeInfoProductProducts (language: String, category: String): Single<MutableList<String>>
    fun getTradeInfoOrigin          (language: String, category: String, product: String): Single<MutableList<String>>
    fun getTradeInfoDestination     (language: String, category: String, product: String, origin: String): Single<MutableList<String>>
    fun getTaxInfoCurrency() :Single<MutableList<ExchangeRateData>>
    fun searchTradeInfoBorderProcedures
                (language: String,
                 category: String,
                 product: String,
                 origin: String,
                 dest: String,
                 value: Double): Single<TradeInfoData>

    fun searchTradeInfoRequiredDocuments
                (language: String,
                 category: String,
                 product: String,
                 origin: String,
                 dest: String,
                 value: Double): Single<TradeInfoData>

    fun searchTradeInfoBorderAgencies
                (language: String,
                 category: String,
                 product: String,
                 origin: String,
                 dest: String,
                 value: Double): Single<TradeInfoData>

    fun searchTradeInfoTaxes
                (language: String,
                 category: String,
                 product: String,
                 origin: String,
                 dest: String,
                 value: Double): Single<MutableList<Taxes>>

    fun getRegulatedGoodsCountries  (language: String): Single<MutableList<String>>
    fun searchRegulatedGoods        (language: String, country: String, regulatedType: String) : Single<RegulatedGoodData>
    fun searchRegulatedProhibiteds  (language: String, country: String): Single<TradeInfoData>
    fun searchRegulatedRestricteds  (language: String, country: String): Single<TradeInfoData>
    fun searchRegulatedSensitives   (language: String, country: String): Single<TradeInfoData>

}