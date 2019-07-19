package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.RecentMarketPriceData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface RecentMarketPriceDao: BaseDao<RecentMarketPriceData> {

    @Query("SELECT * FROM recent_market_prices ORDER BY time_created DESC")
    fun getAllRecentMarketPriceOrderByTimeCreated(): Single<MutableList<RecentMarketPriceData>>

    @Query("SELECT COUNT(*) FROM recent_market_prices")
    fun getCount(): Single<Long>

    @Query("DELETE FROM recent_market_prices WHERE id in (SELECT id FROM recent_market_prices ORDER BY time_created ASC LIMIT :limit)")
    fun deleteOldestRecentMarketPrice(limit: Long): Completable
}