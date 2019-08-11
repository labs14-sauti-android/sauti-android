package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceCache
import com.labs.sauti.cache.MarketPriceSearchCache
import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.market_price.MarketPriceSearchData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MarketPriceRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val marketPriceRoomCache: MarketPriceCache,
    private val marketPriceSearchRoomCache: MarketPriceSearchCache
) : MarketPriceRepository {

    override fun getMarketPriceCountries(): Single<MutableList<String>> {
        return sautiApiService.getMarketPriceCountries()
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
}