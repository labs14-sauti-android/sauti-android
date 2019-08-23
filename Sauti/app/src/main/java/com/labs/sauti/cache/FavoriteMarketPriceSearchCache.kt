package com.labs.sauti.cache

import com.labs.sauti.model.market_price.FavoriteMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteMarketPriceSearchCache {

    fun isFavorite(userId: Long, country: String, market: String, category: String, product: String): Single<Boolean>
    fun addFavorite(favoriteMarketPriceSearch: FavoriteMarketPriceSearchData): Completable
    fun removeFavoriteForced(userId: Long, country: String, market: String, category: String, product: String): Completable
    fun removeFavorite(userId: Long, country: String, market: String, category: String, product: String): Completable
    fun saveAll(favoriteMarketPriceSearches: MutableList<FavoriteMarketPriceSearchData>): Completable
    fun getFavorite(userId: Long, country: String, market: String, category: String, product: String): Single<FavoriteMarketPriceSearchData>
    fun getAll(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>>
    fun getAllNotSynced(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>>
    fun getAllShouldDelete(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>>
    fun deleteAll(userId: Long): Completable

}