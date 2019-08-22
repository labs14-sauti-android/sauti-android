package com.labs.sauti.cache

import com.labs.sauti.model.market_price.FavoriteMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteMarketPriceSearchCache {

    fun isFavorite(country: String, market: String, category: String, product: String): Single<Boolean>
    fun addFavorite(favoriteMarketPriceSearch: FavoriteMarketPriceSearchData): Completable
    fun removeFavoriteForced(country: String, market: String, category: String, product: String): Completable
    fun removeFavorite(country: String, market: String, category: String, product: String): Completable
    fun saveAll(favoriteMarketPriceSearches: MutableList<FavoriteMarketPriceSearchData>): Completable
    fun getFavorite(country: String, market: String, category: String, product: String): Single<FavoriteMarketPriceSearchData>
    fun getAllNotSynced(): Single<MutableList<FavoriteMarketPriceSearchData>>
    fun getAllShouldDelete(): Single<MutableList<FavoriteMarketPriceSearchData>>
    fun deleteAll(): Completable

}