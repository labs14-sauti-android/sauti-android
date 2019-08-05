package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.market_price.MarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MarketPriceSearchDao : BaseDao<MarketPriceSearchData> {

    @Query("SELECT * FROM market_price_searches ORDER BY id DESC")
    fun getAllByIdDesc(): Single<MutableList<MarketPriceSearchData>>

    @Query("SELECT COUNT(*) FROM market_price_searches")
    fun getCount(): Single<Long>

    @Query("SELECT * FROM market_price_searches WHERE country=:country AND market=:market AND category=:category AND product=:product")
    fun getBySearch(country: String, market: String, category: String, product: String): Single<MarketPriceSearchData>

    @Query("DELETE FROM market_price_searches WHERE id in (SELECT id FROM market_price_searches ORDER BY id ASC LIMIT :limit)")
    fun deleteOldest(limit: Long): Completable
}