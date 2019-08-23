package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.labs.sauti.model.exchange_rate.FavoriteExchangeRateConversionData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface FavoriteExchangeRateConversionDao : BaseDao<FavoriteExchangeRateConversionData> {

    @Insert
    fun insertAll(favoriteExchangeRateConversions: List<FavoriteExchangeRateConversionData>): Maybe<MutableList<Long>>

    @Query("SELECT * FROM favorite_exchange_rate_conversions WHERE userId=:userId AND fromCurrency=:fromCurrency AND toCurrency=:toCurrency AND amount=:amount")
    fun getBySearch(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Single<FavoriteExchangeRateConversionData>

    @Query("SELECT COUNT(*) FROM favorite_exchange_rate_conversions WHERE userId=:userId AND fromCurrency=:fromCurrency AND amount=:amount LIMIT 1")
    fun contains(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Single<Long>

    @Query("SELECT * FROM favorite_exchange_rate_conversions WHERE userId=:userId")
    fun findAll(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>>

    @Query("SELECT * FROM favorite_exchange_rate_conversions WHERE userId=:userId AND favoriteExchangeRateConversionId IS NULL")
    fun findAllNotSynced(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>>

    @Query("SELECT * FROM favorite_exchange_rate_conversions WHERE userId=userId AND shouldRemove=1")
    fun findAllShouldDelete(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>>

    @Query("DELETE FROM favorite_exchange_rate_conversions WHERE userId=:userId AND fromCurrency=:fromCurrency AND toCurrency=:toCurrency AND amount=:amount")
    fun deleteBySearch(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Completable

    @Query("DELETE FROM favorite_exchange_rate_conversions WHERE userId=:userId")
    fun deleteAll(userId: Long): Completable

}