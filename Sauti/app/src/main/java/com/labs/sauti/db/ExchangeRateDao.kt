package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.labs.sauti.model.ExchangeRateData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface ExchangeRateDao: BaseDao<ExchangeRateData> {

    @Insert
    fun insert(exchangeRates: MutableList<ExchangeRateData>): Maybe<MutableList<Long>>

    @Query("SELECT * FROM exchange_rates WHERE currency=:currency")
    fun get(currency: String): Single<ExchangeRateData>

    @Query("SELECT * FROM exchange_rates")
    fun getAll(): Single<MutableList<ExchangeRateData>>

    @Query("DELETE FROM exchange_rates")
    fun deleteAll(): Completable

}