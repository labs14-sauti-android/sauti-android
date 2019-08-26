package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.ExchangeRateCache
import com.labs.sauti.cache.ExchangeRateConversionCache
import com.labs.sauti.cache.FavoriteExchangeRateConversionCache
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionData
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResultData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.exchange_rate.FavoriteExchangeRateConversionData
import com.labs.sauti.sp.SessionSp
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ExchangeRateRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val sessionSp: SessionSp,
    private val exchangeRateRoomCache: ExchangeRateCache,
    private val exchangeRateConversionRoomCache: ExchangeRateConversionCache,
    private val favoriteExchangeRateConversionRoomCache: FavoriteExchangeRateConversionCache
) : ExchangeRateRepository {

    override fun getExchangeRates(): Single<MutableList<ExchangeRateData>> {
        return sautiApiService.getExchangeRates()
            .doOnSuccess {
                exchangeRateRoomCache.replaceAll(it).blockingAwait()
            }
            .onErrorResumeNext {
                exchangeRateRoomCache.getAll()
            }
            .doOnSuccess {
                it.sortBy { exchangeRate ->
                    exchangeRate.currency
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun convertCurrency(
        shouldSaveConversion: Boolean,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Single<ExchangeRateConversionResultData> {
        return Single.fromCallable {
            val fromExchangeRate = exchangeRateRoomCache.get(fromCurrency).blockingGet()
            val toExchangeRate = exchangeRateRoomCache.get(toCurrency).blockingGet()

            val toPerFrom = if (toExchangeRate.rate == null || fromExchangeRate.rate == null) {
                0.0
            } else {
                toExchangeRate.rate!! / fromExchangeRate.rate!!
            }

            ExchangeRateConversionResultData(
                fromCurrency,
                toCurrency,
                toPerFrom,
                amount,
                toPerFrom * amount
            )
        }
            .doOnSuccess {
                if (shouldSaveConversion) {
                    exchangeRateConversionRoomCache.save(
                        ExchangeRateConversionData(
                            fromCurrency = fromCurrency,
                            toCurrency = toCurrency,
                            amount = amount
                        )
                    ).blockingAwait()
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getRecentConversionResults(): Single<MutableList<ExchangeRateConversionResultData>> {
        return getExchangeRates() // update the local exchange rates if possible
            .flatMap {
                Single.fromCallable {
                    val conversions = exchangeRateConversionRoomCache.getAll().blockingGet()
                    getConversionResults(conversions)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getRecentConversionResultsInCache(): Single<MutableList<ExchangeRateConversionResultData>> {
        return Single.fromCallable {
            val conversions = exchangeRateConversionRoomCache.getAll().blockingGet()
            getConversionResults(conversions)
        }
            .subscribeOn(Schedulers.io())
    }

    override fun syncFavoriteExchangeRateConversions(userId: Long): Completable {
        return Completable.fromCallable {
            if (sessionSp.isAccessTokenValid()) {
                val accessToken = sessionSp.getAccessToken()
                val authorization = "Bearer $accessToken"

                // add all not-synced to server
                val notSyncedList =
                    favoriteExchangeRateConversionRoomCache.getAllNotSynced(userId).blockingGet()
                sautiApiService.addAllFavoriteExchangeRateConversions(authorization, notSyncedList).blockingGet()

                // delete all shouldRemove in the server
                val shouldDeleteList =
                    favoriteExchangeRateConversionRoomCache.getAllShouldDelete(userId).blockingGet()
                val shouldDeleteIdList = shouldDeleteList.mapNotNull {it.favoriteExchangeRateConversionId}
                sautiApiService.deleteAllFavoriteExchangeRateConversions(authorization, shouldDeleteIdList).blockingAwait()

                // refresh favorites
                val favoriteExchangeRateConversions =
                    sautiApiService.getAllFavoriteExchangeRateConversions(authorization).blockingGet()
                favoriteExchangeRateConversionRoomCache.deleteAll(userId).blockingAwait()
                favoriteExchangeRateConversionRoomCache.saveAll(favoriteExchangeRateConversions).blockingAwait()
            }
        }
    }

    override fun isFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Single<Boolean> {
        return favoriteExchangeRateConversionRoomCache.isFavorite(userId, fromCurrency, toCurrency, amount)
    }

    override fun addToFavorite(userId: Long, fromCurrency: String, toCurrency: String, amount: Double): Completable {
        val favoriteExchangeRateConversionData = FavoriteExchangeRateConversionData(
            userId = userId,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            shouldRemove = 0
        )
        val accessToken = sessionSp.getAccessToken()
        val authorization = "Bearer $accessToken"

        return sautiApiService.addAllFavoriteExchangeRateConversions(authorization, mutableListOf(favoriteExchangeRateConversionData))
            .flatMapCompletable {
                if (it.isNotEmpty()) { // added to the server
                    // add locally with id
                    return@flatMapCompletable favoriteExchangeRateConversionRoomCache.addFavorite(it[0])
                }

                Completable.complete()
            }
            .onErrorResumeNext {
                // add locally
                favoriteExchangeRateConversionRoomCache.addFavorite(favoriteExchangeRateConversionData)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun removeFromFavorite(
        userId: Long,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Completable {
        return favoriteExchangeRateConversionRoomCache.getFavorite(userId, fromCurrency, toCurrency, amount)
            .flatMapCompletable {
                if (it.favoriteExchangeRateConversionId != null) { // synced
                    val accessToken = sessionSp.getAccessToken()
                    val authorization = "Bearer $accessToken"

                    return@flatMapCompletable sautiApiService.deleteAllFavoriteExchangeRateConversions(authorization, mutableListOf(it.favoriteExchangeRateConversionId!!))
                        .doOnComplete {
                            // completely remove
                            favoriteExchangeRateConversionRoomCache.removeFavoriteForced(userId, fromCurrency, toCurrency, amount).blockingAwait()
                        }
                        .onErrorResumeNext {
                            // mark for removal
                            favoriteExchangeRateConversionRoomCache.removeFavorite(userId, fromCurrency, toCurrency, amount)
                        }
                } else {
                    // completely remove
                    return@flatMapCompletable favoriteExchangeRateConversionRoomCache.removeFavoriteForced(userId, fromCurrency, toCurrency, amount)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getFavoriteExchangeRateConversionResults(userId: Long): Single<HashMap<ExchangeRateConversionResultData, Long>> {
        val accessToken = sessionSp.getAccessToken()
        val authorization = "Bearer $accessToken"

        return sautiApiService.getAllFavoriteExchangeRateConversions(authorization)
            .onErrorResumeNext {
                favoriteExchangeRateConversionRoomCache.getAll(userId)
            }
            .map {favoriteExchangeRateConversions ->
                // update local exchange rate if possible
                getExchangeRates().blockingGet()

                val favoriteConversionResultTimestampMap = hashMapOf<ExchangeRateConversionResultData, Long>()
                favoriteExchangeRateConversions.forEach {
                    try {
                        val fromExchangeRate = exchangeRateRoomCache.get(it.fromCurrency ?: "").blockingGet()
                        val toExchangeRate = exchangeRateRoomCache.get(it.toCurrency ?: "").blockingGet()
                        val toPerFrom = if (toExchangeRate.rate == null || fromExchangeRate.rate == null) {
                            0.0
                        } else {
                            toExchangeRate.rate!! / fromExchangeRate.rate!!
                        }

                        val exchangeRateConversionResultData = ExchangeRateConversionResultData(
                            it.fromCurrency ?: "",
                            it.toCurrency ?: "",
                            toPerFrom,
                            it.amount ?: 0.0,
                            toPerFrom * (it.amount ?: 0.0)
                        )

                        favoriteConversionResultTimestampMap[exchangeRateConversionResultData] = it.timestamp ?: 0

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                favoriteConversionResultTimestampMap
            }
            .subscribeOn(Schedulers.io())
    }

    private fun getConversionResults(conversions: List<ExchangeRateConversionData>): MutableList<ExchangeRateConversionResultData> {
        val conversionResults = mutableListOf<ExchangeRateConversionResultData>()
        conversions.forEach {
            try {
                val fromExchangeRate = exchangeRateRoomCache.get(it.fromCurrency).blockingGet()
                val toExchangeRate = exchangeRateRoomCache.get(it.toCurrency).blockingGet()
                val toPerFrom = if (toExchangeRate.rate == null || fromExchangeRate.rate == null) {
                    0.0
                } else {
                    toExchangeRate.rate!! / fromExchangeRate.rate!!
                }

                conversionResults.add(
                    ExchangeRateConversionResultData(
                        it.fromCurrency,
                        it.toCurrency,
                        toPerFrom,
                        it.amount,
                        toPerFrom * it.amount
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return conversionResults
    }
}