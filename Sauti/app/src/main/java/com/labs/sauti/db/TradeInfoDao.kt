package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
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

    @Query("SELECT DISTINCT productCat from trade_info where language=:language")
    fun getTradeInfoCategories(language : String) : Single<MutableList<String>>

}