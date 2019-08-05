package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.market_price.MarketPriceData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MarketPriceDao : BaseDao<MarketPriceData> {

    @Query("SELECT * FROM market_prices")
    fun getAll(): Single<MutableList<MarketPriceData>>

    @Query("SELECT DISTINCT country FROM market_prices")
    fun getCountries(): Single<MutableList<String>>

    @Query("SELECT DISTINCT market FROM market_prices WHERE country=:country")
    fun getMarkets(country: String): Single<MutableList<String>>

    @Query("SELECT DISTINCT product_cat FROM market_prices WHERE country=:country AND market=:market")
    fun getCategories(country: String, market: String): Single<MutableList<String>>

    @Query("SELECT DISTINCT product FROM market_prices WHERE country=:country AND market=:market AND product_cat=:category")
    fun getProducts(country: String, market: String, category: String): Single<MutableList<String>>

    @Query("SELECT * FROM market_prices WHERE country=:country AND market=:market AND product_cat=:category AND product=:product")
    fun getBySearch(country: String, market: String, category: String, product: String): Single<MarketPriceData>

    @Query("DELETE FROM market_prices")
    fun deleteAll(): Completable

}