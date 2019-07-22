package com.labs.sauti.repository

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceRoomCache
import com.labs.sauti.cache.RecentMarketPriceRoomCache
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.mapper.Mapper
import com.labs.sauti.model.*
import com.labs.sauti.sp.SessionSp
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class SautiRepositoryImpl(
    private val networkHelper: NetworkHelper,
    private val sautiApiService: SautiApiService,
    private val sautiAuthorization: String,
    private val sessionSp: SessionSp,
    private val marketPriceRoomCache: MarketPriceRoomCache,
    private val recentMarketPriceRoomCache: RecentMarketPriceRoomCache,
    private val marketPriceDataRecentMarketPriceDataMapper: Mapper<MarketPriceData, RecentMarketPriceData>
) : SautiRepository {
    override fun login(username: String, password: String): Single<LoginResponse> {
        return sautiApiService.login(sautiAuthorization, "password", username, password)
            .doOnSuccess {
                sessionSp.setAccessToken(it.accessToken ?: "")
                sessionSp.setExpiresIn(it.expiresIn ?: 0)
                sessionSp.setLoggedInAt(System.currentTimeMillis() / 1000L)
            }
    }

    override fun signOut(): Single<Unit> {
        // TODO call backend logout
        return Single.fromCallable {
            sessionSp.invalidateToken()
            sessionSp.setUser(null)
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
        // TODO test only
        val network = Single.fromCallable {
            if (!networkHelper.hasNetworkConnection()) throw Exception("No network connection")

            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Exception("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            val countrySet = hashSetOf<String>()
            marketPrices.forEach {
                if (it.country != null) {
                    countrySet.add(it.country!!)
                }
            }
            countrySet.toMutableList()
        }

        return network.onErrorResumeNext {
            marketPriceRoomCache.getCountries()
        }
            .subscribeOn(Schedulers.io())
    }

    override fun getMarketPriceMarkets(country: String): Single<MutableList<String>> {
        // TODO test only
        return Single.fromCallable {
            if (!networkHelper.hasNetworkConnection()) throw Exception("No network connection")

            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Exception("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            val marketSet = hashSetOf<String>()
            marketPrices.forEach {
                if (it.market != null &&
                    it.country == country) {
                    marketSet.add(it.market!!)
                }
            }
            marketSet.toMutableList()
        }
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext {
                marketPriceRoomCache.getMarkets(country)
            }
    }

    override fun getMarketPriceCategories(country: String, market: String): Single<MutableList<String>> {
        // TODO test only
        return Single.fromCallable {
            if (!networkHelper.hasNetworkConnection()) throw Exception("No network connection")

            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Exception("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            val categorySet = hashSetOf<String>()
            marketPrices.forEach {
                if (it.productCat != null &&
                    it.country == country &&
                    it.market == market) {
                    categorySet.add(it.productCat!!)
                }
            }
            categorySet.toMutableList()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .onErrorResumeNext {
                marketPriceRoomCache.getCategories(country, market)
            }
    }

    override fun getMarketPriceProducts(country: String, market: String, category: String): Single<MutableList<String>> {
        // TODO test only

        return Single.fromCallable {
            if (!networkHelper.hasNetworkConnection()) throw Exception("No network connection")

            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Exception("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            val productSet = hashSetOf<String>()
            marketPrices.forEach {
                if (it.product != null &&
                    it.country == country &&
                    it.market == market &&
                    it.productCat == category) {
                    productSet.add(it.product!!)
                }
            }
            productSet.toMutableList()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .onErrorResumeNext {
                marketPriceRoomCache.getProducts(country, market, category)
            }
    }

    override fun searchMarketPrice(country: String, market: String, category: String, product: String): Single<MarketPriceData> {
        // TODO test only
        return Single.fromCallable {
            if (!networkHelper.hasNetworkConnection()) throw Exception("No network connection")

            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Exception("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            marketPrices.forEach {
                if (it.country == country &&
                    it.market == market &&
                    it.productCat == category &&
                    it.product == product) {
                    return@fromCallable it
                }
            }

            throw Exception("Cannot find market price")
        }
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                marketPriceRoomCache.save(it).blockingAwait()
            }
            .onErrorResumeNext {
                marketPriceRoomCache.search(country, market, category, product)
            }
            .doOnSuccess {
                recentMarketPriceRoomCache.save(marketPriceDataRecentMarketPriceDataMapper.mapFrom(it).apply {
                    timeCreated = System.currentTimeMillis()
                }).blockingAwait()
            }
    }

    override fun getRecentMarketPrices(): Single<MutableList<RecentMarketPriceData>> {
        return recentMarketPriceRoomCache.getAll()
    }
}