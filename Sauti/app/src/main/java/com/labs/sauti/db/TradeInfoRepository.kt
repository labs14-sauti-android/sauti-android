package com.labs.sauti.db

import com.labs.sauti.model.TradeInfoData

class TradeInfoRepository(private val tradeInfoDao: TradeInfoDao) {

    //Executiion done on a seperate thread.
    //Observed LiveData will notify the observer when the data has changed.
    //List will be placed into LiveData in the ViewModel
    val allTradeInfo: List<TradeInfoData> = tradeInfoDao.getAllTradeInfo()

    //Must be called from non-ui thread.
    //TODO: Will do a check in the database and add accordingly.
    fun insertTradeInfo(tradeInfoData: TradeInfoData) {
        tradeInfoDao.insert(tradeInfoData)
    }
}