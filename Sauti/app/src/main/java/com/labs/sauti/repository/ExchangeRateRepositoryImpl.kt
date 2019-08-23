package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.ExchangeRateCache
import com.labs.sauti.cache.ExchangeRateConversionCache
import com.labs.sauti.cache.FavoriteExchangeRateConversionCache
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionData
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResultData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.sp.SessionSp
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
                exchangeRateConversionRoomCache.save(
                    ExchangeRateConversionData(
                        fromCurrency = fromCurrency,
                        toCurrency = toCurrency,
                        amount = amount
                    )
                ).blockingAwait()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getRecentConversionResults(): Single<MutableList<ExchangeRateConversionResultData>> {
        return getExchangeRates() // update the local exchange rates if possible
            .flatMap {
                Single.fromCallable {
                    val conversions = exchangeRateConversionRoomCache.getAll().blockingGet()
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
                    conversionResults
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getRecentConversionResultsInCache(): Single<MutableList<ExchangeRateConversionResultData>> {
        return Single.fromCallable {
            val conversions = exchangeRateConversionRoomCache.getAll().blockingGet()
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
            conversionResults
        }
            .subscribeOn(Schedulers.io())
    }
}