package com.labs.sauti.cache

import com.labs.sauti.model.exchange_rate.FavoriteExchangeRateConversionData
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteExchangeRateConversionCache {

    fun isFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Single<Boolean>
    fun addFavorite(favoriteExchangeRateConversion: FavoriteExchangeRateConversionData): Completable
    fun removeFavoriteForced(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Completable
    fun removeFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Completable
    fun saveAll(favoriteExchangeRateConversions: List<FavoriteExchangeRateConversionData>): Completable
    fun getFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Single<FavoriteExchangeRateConversionData>
    fun getAll(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>>
    fun getAllNotSynced(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>>
    fun getAllShouldDelete(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>>
    fun deleteAll(userId: Long): Completable
}