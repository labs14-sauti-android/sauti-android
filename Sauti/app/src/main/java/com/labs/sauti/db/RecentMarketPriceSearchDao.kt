package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.RecentMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface RecentMarketPriceSearchDao : BaseDao<RecentMarketPriceSearchData> {

    @Query("SELECT * FROM recent_market_price_searches ORDER BY id DESC")
    fun getAllByIdDesc(): Single<MutableList<RecentMarketPriceSearchData>>

    @Query("SELECT COUNT(*) FROM recent_market_price_searches")
    fun getCount(): Single<Long>

    @Query("SELECT * FROM recent_market_price_searches WHERE country=:country AND market=:market AND category=:category AND product=:product")
    fun getBySearch(country: String, market: String, category: String, product: String): Single<RecentMarketPriceSearchData>

    @Query("DELETE FROM recent_market_price_searches WHERE id in (SELECT id FROM recent_market_price_searches ORDER BY id ASC LIMIT :limit)")
    fun deleteOldest(limit: Long): Completable
}