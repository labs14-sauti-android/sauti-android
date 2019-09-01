package com.labs.sauti.db

import android.app.VoiceInteractor
import androidx.room.*
import com.labs.sauti.model.trade_info.Procedure
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

//CAN BE REMOVED AND CONNECT DIRECTLY TO DAO

@Dao
interface TradeInfoDao : BaseDao<TradeInfoData> {

    @Query(value = "SELECT * FROM trade_info")
    fun getAllTradeInfo(): List<TradeInfoData>

    @Query("DELETE FROM trade_info")
    fun deleteAll() : Completable

    @Query("SELECT DISTINCT regulatedCountry FROM trade_info WHERE language=:language AND regulatedCountry IS NOT NULL")
    fun getRegulatedCountries(language: String): Single<MutableList<String>>

    @Query("SELECT * from trade_info WHERE language=:language AND regulatedCountry=:regulatedCountry and prohibiteds IS NOT NULL")
    fun getRegulatedProhibited(language: String, regulatedCountry: String): Single<TradeInfoData>

    @Query("SELECT * from trade_info WHERE language=:language AND regulatedCountry=:regulatedCountry and restricteds IS NOT NULL")
    fun getRegulatedRestricted(language: String, regulatedCountry: String): Single<TradeInfoData>
    @Query("SELECT * from trade_info WHERE language=:language AND regulatedCountry=:regulatedCountry and sensitives IS NOT NULL")
    fun getRegulatedSensitives(language: String, regulatedCountry: String): Single<TradeInfoData>

    @Query("SELECT DISTINCT productCat FROM trade_info WHERE language=:language and productCat IS NOT NULL")
    fun getTradeInfoProductCategories(language : String) : Single<MutableList<String>>

    @Query("SELECT DISTINCT product FROM trade_info WHERE language=:language AND productCat=:productCat AND product IS NOT NULL")
    fun getTradeInfoProducts(language : String, productCat: String) : Single<MutableList<String>>

    @Query("SELECT DISTINCT origin FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin IS NOT NULL")
    fun getTradeInfoOrigin(language : String,
                           productCat: String,
                           product: String) : Single<MutableList<String>>

    @Query("SELECT DISTINCT dest FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest IS NOT NULL")
    fun getTradeInfoDestination(language : String,
                                productCat: String,
                                product: String,
                                origin: String) : Single<MutableList<String>>

    @Query("SELECT DISTINCT userCurrency FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND taxes IS NOT NULL")
    fun getTaxCalculatorUserCurrency(language : String,
                                     productCat: String,
                                     product: String,
                                     origin: String): Single<MutableList<String>>

    @Query("SELECT * FROM trade_info WHERE taxes IS NULL ORDER BY trade_info_id DESC LIMIT 2")
    fun getTwoMostRecentTradeInfo(): Single<MutableList<TradeInfoData>>

    @Query("SELECT * FROM trade_info WHERE taxes IS NOT NULL ORDER BY trade_info_id DESC LIMIT 2")
    fun getTwoMostRecentTradeInfoTaxes(): Single<MutableList<TradeInfoData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTradeInfo(newest: TradeInfoData): Completable

    @Transaction
    fun insertThenDeleteTradeInfo(old: TradeInfoData, new: TradeInfoData) {
        insert(new)
        delete(old)
    }

    @Query("SELECT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value and  procedures IS NOT NULL")
    fun getTradeInfoProcedures(language : String,
                               productCat: String,
                               product: String,
                               origin: String,
                               dest: String,
                               value: String) : Single<TradeInfoData>

    @Query("SELECT DISTINCT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND relevantAgencyData IS NOT NULL")
    fun getTradeInfoBorderAgencies(language : String,
                                   productCat: String,
                                   product: String,
                                   origin: String,
                                   dest: String,
                                   value: String) : Single<TradeInfoData>

    @Query("SELECT DISTINCT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND requiredDocumentData IS NOT NULL")
    fun getTradeInfoRequiredDocuments(language : String,
                                      productCat: String,
                                      product: String,
                                      origin: String,
                                      dest: String,
                                      value: String) : Single<TradeInfoData>

    @Query("SELECT DISTINCT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND userCurrency=:userCurrency AND destinationCurrency=:destCurrency AND taxes IS NOT NULL")
    fun getTradeInfoTaxes(language : String,
                          productCat: String,
                          product: String,
                          origin: String,
                          dest: String,
                          value: String,
                          userCurrency: String,
                          destCurrency: String) : Single<TradeInfoData>

    @Query("SELECT * FROM trade_info WHERE language=:language AND dest=:country AND procedures!=NULL")
    fun getTradeInfoRegulatedGoodsProhibited(language: String, country: String): Single<TradeInfoData>

}