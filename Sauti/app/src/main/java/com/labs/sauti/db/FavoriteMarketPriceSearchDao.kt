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
    @Query("SELECT * FROM favorite_market_price_searches WHERE country=:country AND market=:market AND category=:category AND product=:product")
    fun getBySearch(country: String, market: String, category: String, product: String): Single<FavoriteMarketPriceSearchData>

    @Query("SELECT COUNT(*) FROM favorite_market_price_searches WHERE country=:country AND market=:market AND category=:category AND product=:product LIMIT 1")
    fun contains(country: String, market: String, category: String, product: String): Single<Long>

    @Query("SELECT * FROM favorite_market_price_searches WHERE favoriteMarketPriceSearchId=NULL")
    fun findAllNotSynced(): Single<MutableList<FavoriteMarketPriceSearchData>>

    @Query("SELECT * FROM favorite_market_price_searches WHERE shouldRemove=1")
    fun findAllShouldDelete(): Single<MutableList<FavoriteMarketPriceSearchData>>

    @Query("DELETE FROM favorite_market_price_searches WHERE country=:country AND market=:market AND category=:category AND product=:product")
    fun deleteBySearch(country: String, market: String, category: String, product: String): Completable

    @Query("DELETE FROM favorite_market_price_searches")
    fun deleteAll(): Completable
}