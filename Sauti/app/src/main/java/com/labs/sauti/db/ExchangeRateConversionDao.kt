package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExchangeRateConversionDao: BaseDao<ExchangeRateConversionData> {
    @Query("SELECT * FROM exchange_rate_conversions ORDER BY id DESC")
    fun getAllOrderByIdDesc(): Single<MutableList<ExchangeRateConversionData>>

    @Query("SELECT COUNT(*) FROM exchange_rate_conversions")
    fun getCount(): Single<Long>

    @Query("DELETE FROM exchange_rate_conversions WHERE id in (SELECT id FROM exchange_rate_conversions ORDER BY id ASC LIMIT :limit)")
    fun deleteOldest(limit: Long): Completable

}