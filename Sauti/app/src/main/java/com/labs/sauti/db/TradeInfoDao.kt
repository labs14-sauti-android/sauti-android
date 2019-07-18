package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.TradeInfoData

//CAN BE REMOVED AND CONNECT DIRECTLY TO DAO

@Dao
interface TradeInfoDao : BaseDao<TradeInfoData> {

    //Executiion done on a seperate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //List will be placed into LiveData in the ViewModel
    @Query(value = "SELECT * FROM trade_info")
    fun getAllTradeInfo(): List<TradeInfoData>

    @Query("DELETE FROM trade_info")
    fun deleteAll()

}