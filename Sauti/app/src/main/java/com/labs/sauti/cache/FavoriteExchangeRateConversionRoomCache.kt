package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.exchange_rate.FavoriteExchangeRateConversionData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FavoriteExchangeRateConversionRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : FavoriteExchangeRateConversionCache {
    private val dao = sautiRoomDatabase.favoriteExchangeRateConversionDao()

    override fun isFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Single<Boolean> {
        return dao.contains(userId, fromCurrency, toCurrency, amount)
            .map {
                if (it > 0L) {
                    val favoriteExchangeRateConversionData =
                        dao.getBySearch(userId, fromCurrency, toCurrency, amount).blockingGet()
                    if (favoriteExchangeRateConversionData.shouldRemove == 0) return@map true
                }

                return@map false
            }
            .subscribeOn(Schedulers.io())
    }

    override fun addFavorite(favoriteExchangeRateConversion: FavoriteExchangeRateConversionData): Completable {
        return dao.contains(
            favoriteExchangeRateConversion.userId!!,
            favoriteExchangeRateConversion.fromCurrency!!,
            favoriteExchangeRateConversion.toCurrency!!,
            favoriteExchangeRateConversion.amount!!
        )
            .flatMapCompletable {
                if (it > 0L) { // already favorite
                    val foundFavoriteExchangeRateConversion =
                        dao.getBySearch(
                            favoriteExchangeRateConversion.userId!!,
                            favoriteExchangeRateConversion.fromCurrency!!,
                            favoriteExchangeRateConversion.toCurrency!!,
                            favoriteExchangeRateConversion.amount!!
                        ).blockingGet()

                    if (foundFavoriteExchangeRateConversion.shouldRemove == 1) { // not really
                        foundFavoriteExchangeRateConversion.shouldRemove = 0
                        dao.update(foundFavoriteExchangeRateConversion).blockingAwait()
                    }
                } else {
                    dao.insert(
                        FavoriteExchangeRateConversionData(
                            userId = favoriteExchangeRateConversion.userId,
                            favoriteExchangeRateConversionId = favoriteExchangeRateConversion.favoriteExchangeRateConversionId,
                            fromCurrency = favoriteExchangeRateConversion.fromCurrency,
                            toCurrency = favoriteExchangeRateConversion.toCurrency,
                            amount = favoriteExchangeRateConversion.amount,
                            shouldRemove = 0
                        )
                    ).blockingGet()
                }

                Completable.complete()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun removeFavoriteForced(
        userId: Long,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Completable {
        return dao.deleteBySearch(userId, fromCurrency, toCurrency, amount)
            .subscribeOn(Schedulers.io())
    }

    override fun removeFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Completable {
        return dao.getBySearch(userId, fromCurrency, toCurrency, amount)
            .flatMapCompletable {
                if (it.favoriteExchangeRateConversionId != null) { // synced
                    it.shouldRemove = 1
                    dao.update(it)
                } else { // not synced
                    dao.delete(it)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun saveAll(favoriteExchangeRateConversions: List<FavoriteExchangeRateConversionData>): Completable {
        return dao.insertAll(favoriteExchangeRateConversions)
            .flatMapCompletable { Completable.complete() }
            .subscribeOn(Schedulers.io())
    }

    override fun getFavorite(
        userId: Long,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Single<FavoriteExchangeRateConversionData> {
        return dao.getBySearch(userId, fromCurrency, toCurrency, amount)
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>> {
        return dao.findAll(userId)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllNotSynced(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>> {
        return dao.findAllNotSynced(userId)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllShouldDelete(userId: Long): Single<MutableList<FavoriteExchangeRateConversionData>> {
        return dao.findAllShouldDelete(userId)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteAll(userId: Long): Completable {
        return dao.deleteAll(userId)
            .subscribeOn(Schedulers.io())
    }

}