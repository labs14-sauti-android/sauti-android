package com.labs.sauti.db

import androidx.room.Insert
import com.labs.sauti.model.trade_info.TradeInfo
import io.reactivex.Completable

//TODO: Create model for TradeInfoSearch
interface FavoriteTradeInfoDao: BaseDao<TradeInfo> {

    @Insert
    fun insertAllTIFavorites(favorites: List<TradeInfo>): Completable
}