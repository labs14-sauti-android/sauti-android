package com.labs.sauti.repository

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.*
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.*
import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.market_price.MarketPriceSearchData
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionData
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResultData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.sp.SettingsSp
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class SautiRepositoryImpl(
    private val networkHelper: NetworkHelper,
    private val sautiApiService: SautiApiService,
    private val sautiAuthorization: String,
    private val sessionSp: SessionSp,
    private val settingsSp: SettingsSp,
    private val marketPriceRoomCache: MarketPriceRoomCache,
    private val marketPriceSearchRoomCache: MarketPriceSearchRoomCache,
    private val exchangeRateRoomCache: ExchangeRateCache,
    private val exchangeRateConversionRoomCache: ExchangeRateConversionCache
) : SautiRepository {

    override fun signUp(signUpRequest: SignUpRequest): Single<SignUpResponse> {
        return sautiApiService.signUp(signUpRequest)
    }

    override fun signIn(username: String, password: String): Single<SignInResponse> {
        return sautiApiService.signIn(sautiAuthorization, "password", username, password)
            .doOnSuccess {
                sessionSp.setAccessToken(it.accessToken ?: "")
                sessionSp.setExpiresIn(it.expiresIn ?: 0)
                sessionSp.setLoggedInAt(System.currentTimeMillis() / 1000L)
            }
    }

    override fun signOut(): Completable {
        return if (sessionSp.isAccessTokenValid()) {
            sautiApiService.signOut(sessionSp.getAccessToken())
            Completable.fromCallable {
                sessionSp.invalidateToken()
                sessionSp.setUser(null)
            }
        } else {
            Completable.complete()
        }
    }

    override fun isAccessTokenValid(): Single<Boolean> {
        return Single.just(sessionSp.isAccessTokenValid())
    }

    override fun getCurrentUser(): Single<User> {
        if (sessionSp.isAccessTokenValid()) {
            val user = sessionSp.getUser()
            if (user != null) return Single.just(user)

            return sautiApiService.getCurrentUser("Bearer ${sessionSp.getAccessToken()}")
                .doOnSuccess {
                    sessionSp.setUser(it)
                }
        }

        // TODO test this and see if it actually does what I think it does
        return Single.error(Throwable())
    }

    override fun getMarketPriceCountries(): Single<MutableList<String>> {
        return sautiApiService.getMarketPriceCountries()
            .map {
                it.data ?: mutableListOf()
            }
            .onErrorResumeNext {
            marketPriceRoomCache.getCountries()
            }
            .doOnSuccess {
                it.sort()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getMarketPriceMarkets(country: String): Single<MutableList<String>> {
        return sautiApiService.getMarketPriceMarkets(country)
            .map {
                it.data ?: mutableListOf()
            }
            .onErrorResumeNext {
                marketPriceRoomCache.getMarkets(country)
            }
            .doOnSuccess {
                it.sort()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getMarketPriceCategories(country: String, market: String): Single<MutableList<String>> {
        return sautiApiService.getMarketPriceCategories(country, market)
            .map {
                it.data ?: mutableListOf()
            }
            .onErrorResumeNext {
                marketPriceRoomCache.getCategories(country, market)
            }
            .doOnSuccess {
                it.sort()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getMarketPriceProducts(country: String, market: String, category: String): Single<MutableList<String>> {
        return sautiApiService.getMarketPriceProducts(country, market, category)
            .map {
                it.data ?: mutableListOf()
            }
            .onErrorResumeNext {
                marketPriceRoomCache.getProducts(country, market, category)
            }
            .doOnSuccess {
                it.sort()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun searchMarketPrice(country: String, market: String, category: String, product: String): Single<MarketPriceData> {
        return sautiApiService.searchMarketPrice(country, market, category, product)
            .doOnSuccess {
                marketPriceRoomCache.save(it).blockingAwait()
            }
            .onErrorResumeNext {
                marketPriceRoomCache.search(country, market, category, product)
            }
            .doOnSuccess {
                marketPriceSearchRoomCache.save(
                    MarketPriceSearchData(
                        country = country,
                        market = market,
                        category = category,
                        product = product
                    )
                ).blockingAwait()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getRecentMarketPriceSearches(): Single<MutableList<MarketPriceSearchData>> {
        return marketPriceSearchRoomCache.getAll()
    }

    /** Search all stored recent market price search data in the network or cache*/
    override fun searchRecentMarketPrices(): Single<MutableList<MarketPriceData>> {
        return marketPriceSearchRoomCache.getAll()
            .flatMap {
                Single.fromCallable {
                    val recentMarketPrices = mutableListOf<MarketPriceData>()
                    it.forEach {
                        try {
                            val marketPrice = searchMarketPrice(it.country, it.market, it.category, it.product).blockingGet()
                            recentMarketPrices.add(marketPrice)
                        } catch (e: Exception) {}
                    }
                    recentMarketPrices
                }
            }
    }

    /** Search all stored recent market price search data only in the cache*/
    override fun searchRecentMarketPriceInCache(): Single<MutableList<MarketPriceData>> {
        return marketPriceSearchRoomCache.getAll()
            .flatMap {
                Single.fromCallable {
                    val recentMarketPriceInCache = mutableListOf<MarketPriceData>()
                    it.forEach {
                        try {
                            val marketPrice = marketPriceRoomCache.search(it.country, it.market, it.category, it.product).blockingGet()
                            recentMarketPriceInCache.add(marketPrice)
                        } catch (e: Exception) {}
                    }
                    recentMarketPriceInCache
                }
            }
    }

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

    override fun getSelectedLanguage(): Single<String> {
        return Single.just(settingsSp.getSelectedLanguage())
            .subscribeOn(Schedulers.io())
    }

    override fun setSelectedLanguage(language: String): Completable {
        return Completable.fromCallable {
            settingsSp.setSelectedLanguage(language)
        }
    }
}