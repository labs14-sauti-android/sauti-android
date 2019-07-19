package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.RecentMarketPriceData

@Dao
interface RecentMarketPriceDao: BaseDao<RecentMarketPriceData> {

    @Query("SELECT * FROM recent_market_prices ORDER BY time_created DESC")
    fun getAllRecentMarketPriceOrderByTimeCreated(): MutableList<RecentMarketPriceData>

    @Query("SELECT COUNT(*) FROM recent_market_prices")
    fun getCount(): Long

    @Query("DELETE FROM recent_market_prices WHERE id in (SELECT id FROM recent_market_prices ORDER BY time_created ASC LIMIT :limit)")
    fun deleteOldestRecentMarketPrice(limit: Long)

    @Query("SELECT * FROM recent_market_prices")
    fun getAll(): MutableList<RecentMarketPriceData>
}