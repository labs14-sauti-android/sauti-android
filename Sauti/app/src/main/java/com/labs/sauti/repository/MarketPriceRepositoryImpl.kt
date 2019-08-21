package com.labs.sauti.repository

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceCache
import com.labs.sauti.cache.MarketPriceSearchCache
import com.labs.sauti.math.LatLon
import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.market_price.MarketPriceSearchData
import com.labs.sauti.model.market_price.MarketplaceData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.NumberFormatException

class MarketPriceRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val marketPriceRoomCache: MarketPriceCache,
    private val marketPriceSearchRoomCache: MarketPriceSearchCache
) : MarketPriceRepository {

    override fun updateMarketPrices(): Completable {
        return Completable.fromCallable {
            // TODO
        }
    }

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
            .doOnSuccess {marketPrice ->
                // TODO test only
                val request = Request.Builder()
                    .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketplaces/&type=json")
                    .build()
                val responseBody = OkHttpClient.Builder().build()
                    .newCall(request)
                    .execute()
                    .body()

                responseBody ?: throw Exception("No response")

                val responseStr = responseBody.string()

                val type = object: TypeToken<MutableList<MarketplaceData>>() {}.type
                val marketplaces = GsonBuilder().create().fromJson<MutableList<MarketplaceData>>(responseStr, type)

                for ((i, currentMarketplace) in marketplaces.withIndex()) {
                    if (currentMarketplace.name == market) {
                        marketplaces.removeAt(i)

                        try {
                            val lat1 = currentMarketplace.lat?.toDouble() ?: throw NumberFormatException()
                            val lon1 = currentMarketplace.lon?.toDouble() ?: throw NumberFormatException()

                            marketplaces.forEach {marketplace ->
                                try {
                                    val lat2 = marketplace.lat?.toDouble() ?: return@forEach
                                    val lon2 = marketplace.lon?.toDouble() ?: return@forEach

                                    val d = LatLon.distance(lat1, lon1, lat2, lon2)
                                    if (d <= 50.0) marketPrice.nearbyMarketplaceNames
                                        .add(marketplace.name ?: return@forEach)

                                    // Busia is 450 km from Arusha

                                } catch (e: NumberFormatException) {}
                            }
                        } catch (e: NumberFormatException) {}

                        break
                    }
                }

                marketPriceRoomCache.save(marketPrice).blockingAwait()
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