package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.market_price.FavoriteMarketPriceSearchData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FavoriteMarketPriceSearchRoomCache(sautiRoomDatabase: SautiRoomDatabase): FavoriteMarketPriceSearchCache {
    private val dao = sautiRoomDatabase.favoriteMarketPriceSearchDao()

    override fun isFavorite(country: String, market: String, category: String, product: String): Single<Boolean> {
        return dao.contains(country, market, category, product)
            .map {
                it > 0L
            }
            .subscribeOn(Schedulers.io())
    }

    override fun addFavorite(favoriteMarketPriceSearch: FavoriteMarketPriceSearchData): Completable {
        return isFavorite(
            favoriteMarketPriceSearch.country!!,
            favoriteMarketPriceSearch.market!!,
            favoriteMarketPriceSearch.category!!,
            favoriteMarketPriceSearch.product!!
        )
            .flatMapCompletable {
                if (it) { // already favorite
                    val foundFavoriteMarketPriceSearch =
                        dao.getBySearch(
                            favoriteMarketPriceSearch.country!!,
                            favoriteMarketPriceSearch.market!!,
                            favoriteMarketPriceSearch.category!!,
                            favoriteMarketPriceSearch.product!!
                        ).blockingGet()

                    if (foundFavoriteMarketPriceSearch.shouldRemove == 1) { // not really
                        foundFavoriteMarketPriceSearch.favoriteMarketPriceSearchId = favoriteMarketPriceSearch.favoriteMarketPriceSearchId
                        foundFavoriteMarketPriceSearch.shouldRemove = 0
                        dao.update(foundFavoriteMarketPriceSearch).blockingAwait()
                    }
                } else {
                    dao.insert(
                        FavoriteMarketPriceSearchData(
                            favoriteMarketPriceSearchId = favoriteMarketPriceSearch.favoriteMarketPriceSearchId,
                            country = favoriteMarketPriceSearch.country,
                            market = favoriteMarketPriceSearch.market,
                            category = favoriteMarketPriceSearch.category,
                            product = favoriteMarketPriceSearch.product,
                            shouldRemove = 0
                    )).blockingGet()
                }

                Completable.complete()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun removeFavoriteForced(country: String, market: String, category: String, product: String): Completable {
        return dao.deleteBySearch(country, market, category, product)
            .subscribeOn(Schedulers.io())
    }

    override fun removeFavorite(country: String, market: String, category: String, product: String): Completable {
        return dao.getBySearch(country, market, category, product)
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
        country: String,
        market: String,
        category: String,
        product: String
    ): Single<FavoriteMarketPriceSearchData> {
        return dao.getBySearch(country, market, category, product)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllNotSynced(): Single<MutableList<FavoriteMarketPriceSearchData>> {
        return dao.findAllNotSynced()
            .subscribeOn(Schedulers.io())
    }

    override fun getAllShouldDelete(): Single<MutableList<FavoriteMarketPriceSearchData>> {
        return dao.findAllShouldDelete()
            .subscribeOn(Schedulers.io())
    }

    override fun deleteAll(): Completable {
        return dao.deleteAll()
            .subscribeOn(Schedulers.io())
    }
}