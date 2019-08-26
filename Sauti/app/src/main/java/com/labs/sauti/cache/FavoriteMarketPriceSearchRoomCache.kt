package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.market_price.FavoriteMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FavoriteMarketPriceSearchRoomCache(sautiRoomDatabase: SautiRoomDatabase): FavoriteMarketPriceSearchCache {
    private val dao = sautiRoomDatabase.favoriteMarketPriceSearchDao()

    override fun isFavorite(userId: Long, country: String, market: String, category: String, product: String): Single<Boolean> {
        return dao.contains(userId, country, market, category, product)
            .map {
                if (it > 0L) {
                    val favoriteMarketPriceSearch =
                        dao.getBySearch(userId, country, market, category, product).blockingGet()
                    if (favoriteMarketPriceSearch.shouldRemove == 0) return@map true
                }

                return@map false
            }
            .subscribeOn(Schedulers.io())
    }

    override fun addFavorite(favoriteMarketPriceSearch: FavoriteMarketPriceSearchData): Completable {
        return dao.contains(
            favoriteMarketPriceSearch.userId!!,
            favoriteMarketPriceSearch.country!!,
            favoriteMarketPriceSearch.market!!,
            favoriteMarketPriceSearch.category!!,
            favoriteMarketPriceSearch.product!!
        )
            .flatMapCompletable {
                if (it > 0L) { // already favorite
                    val foundFavoriteMarketPriceSearch =
                        dao.getBySearch(
                            favoriteMarketPriceSearch.userId!!,
                            favoriteMarketPriceSearch.country!!,
                            favoriteMarketPriceSearch.market!!,
                            favoriteMarketPriceSearch.category!!,
                            favoriteMarketPriceSearch.product!!
                        ).blockingGet()

                    if (foundFavoriteMarketPriceSearch.shouldRemove == 1) { // not really
                        foundFavoriteMarketPriceSearch.shouldRemove = 0
                        foundFavoriteMarketPriceSearch.timestamp = favoriteMarketPriceSearch.timestamp
                        dao.update(foundFavoriteMarketPriceSearch).blockingAwait()
                    }
                } else {
                    dao.insert(
                        FavoriteMarketPriceSearchData(
                            userId = favoriteMarketPriceSearch.userId,
                            favoriteMarketPriceSearchId = favoriteMarketPriceSearch.favoriteMarketPriceSearchId,
                            country = favoriteMarketPriceSearch.country,
                            market = favoriteMarketPriceSearch.market,
                            category = favoriteMarketPriceSearch.category,
                            product = favoriteMarketPriceSearch.product,
                            timestamp = favoriteMarketPriceSearch.timestamp,
                            shouldRemove = 0
                    )).blockingGet()
                }

                Completable.complete()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun removeFavoriteForced(userId: Long, country: String, market: String, category: String, product: String): Completable {
        return dao.deleteBySearch(userId, country, market, category, product)
            .subscribeOn(Schedulers.io())
    }

    override fun removeFavorite(userId: Long, country: String, market: String, category: String, product: String): Completable {
        return dao.getBySearch(userId, country, market, category, product)
            .flatMapCompletable {
                if (it.favoriteMarketPriceSearchId != null) { // syched
                    it.shouldRemove = 1
                    dao.update(it)
                } else { // not synched
                    dao.delete(it)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun saveAll(favoriteMarketPriceSearches: MutableList<FavoriteMarketPriceSearchData>): Completable {
        return dao.insertAll(favoriteMarketPriceSearches)
            .flatMapCompletable { Completable.complete() }
            .subscribeOn(Schedulers.io())
    }

    override fun getFavorite(
        userId: Long,
        country: String,
        market: String,
        category: String,
        product: String
    ): Single<FavoriteMarketPriceSearchData> {
        return dao.getBySearch(userId, country, market, category, product)
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>> {
        return dao.findAll(userId)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllNotSynced(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>> {
        return dao.findAllNotSynced(userId)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllShouldDelete(userId: Long): Single<MutableList<FavoriteMarketPriceSearchData>> {
        return dao.findAllShouldDelete(userId)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteAll(userId: Long): Completable {
        return dao.deleteAll(userId)
            .subscribeOn(Schedulers.io())
    }
}