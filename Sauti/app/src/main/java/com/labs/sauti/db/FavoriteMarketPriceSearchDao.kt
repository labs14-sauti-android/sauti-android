package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.labs.sauti.model.market_price.FavoriteMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface FavoriteMarketPriceSearchDao : BaseDao<FavoriteMarketPriceSearchData> {

    @Insert
    fun insertAll(favoriteMarketPriceSearches: List<FavoriteMarketPriceSearchData>): Maybe<MutableList<Long>>

    /** Can still be shouldRemove*/
    @Query("SELECT * FROM favorite_market_price_searches WHERE userId=:userId AND country=:country AND market=:market AND category=:category AND product=:product")
    fun getBySearch(userId: Long, country: String, market: String, category: String, product: String): Single<FavoriteMarketPriceSearchData>

    @Query("SELECT COUNT(*) FROM favorite_market_price_searches WHERE userId=:userId AND country=:country AND market=:market AND category=:category AND product=:product LIMIT 1")
    fun contains(userId: Long, country: String, market: String, category: String, product: String): Single<Long>

    @Query("SELECT * FROM favorite_market_price_searches WHERE userId=:userId")
    fun findAll(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>>

    @Query("SELECT * FROM favorite_market_price_searches WHERE userId=:userId AND favoriteMarketPriceSearchId IS NULL")
    fun findAllNotSynced(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>>

    @Query("SELECT * FROM favorite_market_price_searches WHERE userId=:userId AND shouldRemove=1")
    fun findAllShouldDelete(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>>

    @Query("DELETE FROM favorite_market_price_searches WHERE userId=:userId AND country=:country AND market=:market AND category=:category AND product=:product")
    fun deleteBySearch(userId: Long, country: String, market: String, category: String, product: String): Completable

    @Query("DELETE FROM favorite_market_price_searches WHERE userId=:userId")
    fun deleteAll(userId: Long): Completable
}