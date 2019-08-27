package com.labs.sauti.db

import android.app.VoiceInteractor
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.labs.sauti.model.trade_info.Procedure
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoData
import io.reactivex.Completable
import io.reactivex.Single

//CAN BE REMOVED AND CONNECT DIRECTLY TO DAO

@Dao
interface TradeInfoDao : BaseDao<TradeInfoData> {

    @Query(value = "SELECT * FROM trade_info")
    fun getAllTradeInfo(): List<TradeInfoData>

    @Query("DELETE FROM trade_info")
    fun deleteAll() : Completable

    @Query("SELECT DISTINCT productCat from trade_info WHERE language=:language")
    fun getTradeInfoProductCategories(language : String) : Single<MutableList<String>>

    @Query("SELECT DISTINCT product FROM trade_info WHERE language=:language AND productCat=:productCat")
    fun getTradeInfoProducts(language : String, productCat: String) : Single<MutableList<String>>

    @Query("SELECT DISTINCT origin FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product")
    fun getTradeInfoOrigin(language : String, productCat: String, product: String) : Single<MutableList<String>>

    @Query("SELECT DISTINCT dest FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin")
    fun getTradeInfoDestination(language : String, productCat: String, product: String, origin: String) : Single<MutableList<String>>

    @Transaction
    fun insertThenDeleteProcedures(old: TradeInfoData, new: TradeInfoData) {
        insert(new)
        delete(old)
    }

    @Query("SELECT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND procedures IS NOT NULL")
    fun getTradeInfoProcedures(language : String, productCat: String, product: String, origin: String, dest: String, value: String) : Single<TradeInfoData>

    @Query("SELECT procedures FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value")
    fun getTradeInfoProceduresList(language : String, productCat: String, product: String, origin: String, dest: String, value: String) : Single<List<Procedure>>

//    @Query("SELECT procedures FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND procedures IS NOT NULL")
//    fun getTradeInfoProceduresList(language : String, productCat: String, product: String, origin: String, dest: String, value: String) : Single<List<Procedure>>

    @Query("SELECT DISTINCT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND relevantAgencyData IS NOT NULL")
    fun getTradeInfoBorderAgencies(language : String, productCat: String, product: String, origin: String, dest: String, value: String) : Single<TradeInfoData>

    @Query("SELECT DISTINCT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND requiredDocumentData IS NOT NULL")
    fun getTradeInfoRequiredDocuments(language : String, productCat: String, product: String, origin: String, dest: String, value: String) : Single<TradeInfoData>

    @Query("SELECT DISTINCT * FROM trade_info WHERE language=:language AND productCat=:productCat AND product=:product AND origin=:origin AND dest=:dest AND value=:value AND taxes IS NOT NULL")
    fun getTradeInfoTaxes(language : String, productCat: String, product: String, origin: String, dest: String, value: String) : Single<TradeInfoData>

    @Query("SELECT * FROM trade_info WHERE language=:language AND dest=:country AND procedures!=NULL")
    fun getTradeInfoRegulatedGoodsProhibited(language: String, country: String): Single<TradeInfoData>

}