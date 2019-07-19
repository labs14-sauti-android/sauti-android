package com.labs.sauti.repository

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceRoomCache
import com.labs.sauti.cache.RecentMarketPriceRoomCache
import com.labs.sauti.mapper.Mapper
import com.labs.sauti.mapper.MarketPriceDataRecentMarketPriceDataMapper
import com.labs.sauti.model.*
import com.labs.sauti.sp.SessionSp
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class SautiRepositoryImpl(
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
        return Single.fromCallable {
            throw Throwable("Not logged in")
        }
    }

    override fun getMarketPriceCountries(): Single<MutableList<MarketPriceCountry>> {
        // TODO test only
        return Single.fromCallable {
            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Throwable("No response")

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
            val countries = mutableListOf<MarketPriceCountry>()
            countrySet.forEach {
                countries.add(MarketPriceCountry(it))
            }
            countries.sortBy { it.country }
            countries.toMutableList()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    }

    override fun getMarketPriceMarkets(marketPriceCountry: MarketPriceCountry): Single<MutableList<MarketPriceMarket>> {
        // TODO test only
        return Single.fromCallable {
            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Throwable("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            val marketSet = hashSetOf<String>()
            marketPrices.forEach {
                if (it.market != null &&
                    it.country == marketPriceCountry.country) {
                    marketSet.add(it.market!!)
                }
            }
            val markets = mutableListOf<MarketPriceMarket>()
            marketSet.forEach {
                markets.add(MarketPriceMarket(it))
            }
            markets.sortBy { it.market }
            markets.toMutableList()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    override fun getMarketPriceCategories(
        marketPriceCountry: MarketPriceCountry,
        marketPriceMarket: MarketPriceMarket
    ): Single<MutableList<MarketPriceCategory>> {
        // TODO test only
        return Single.fromCallable {
            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Throwable("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            val categorySet = hashSetOf<String>()
            marketPrices.forEach {
                if (it.productCat != null &&
                    it.country == marketPriceCountry.country &&
                    it.market == marketPriceMarket.market) {
                    categorySet.add(it.productCat!!)
                }
            }
            val categories = mutableListOf<MarketPriceCategory>()
            categorySet.forEach {
                categories.add(MarketPriceCategory(it))
            }
            categories.sortBy { it.category }
            categories.toMutableList()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    override fun getMarketPriceCommodities(
        marketPriceCountry: MarketPriceCountry,
        marketPriceMarket: MarketPriceMarket,
        marketPriceCategory: MarketPriceCategory
    ): Single<MutableList<MarketPriceCommodity>> {
        // TODO test only
        return Single.fromCallable {
            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Throwable("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            val commoditySet = hashSetOf<String>()
            marketPrices.forEach {
                if (it.product != null &&
                    it.country == marketPriceCountry.country &&
                    it.market == marketPriceMarket.market &&
                    it.productCat == marketPriceCategory.category) {
                    commoditySet.add(it.product!!)
                }
            }
            val commodities = mutableListOf<MarketPriceCommodity>()
            commoditySet.forEach {
                commodities.add(MarketPriceCommodity(it))
            }
            commodities.sortBy { it.commodity }
            commodities.toMutableList()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    override fun searchMarketPrice(country: String, market: String, category: String, commodity: String): Single<MarketPriceData> {
        // TODO test only
        return Single.fromCallable {
            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/marketPrices/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Throwable("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<MarketPriceData>>() {}.type
            val marketPrices = gson.fromJson<MutableList<MarketPriceData>>(responseStr, typeToken)
            marketPrices.forEach {
                if (it.country == country &&
                    it.market == market &&
                    it.productCat == category &&
                    it.product == commodity) {
                    return@fromCallable it
                }
            }

            throw Throwable("Cannot find market price")
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSuccess {
                marketPriceRoomCache.save(it)
                recentMarketPriceRoomCache.save(marketPriceDataRecentMarketPriceDataMapper.mapFrom(it).apply {
                    timeCreated = System.currentTimeMillis()
                })
            }
    }

    override fun getRecentMarketPrices(): Single<MutableList<RecentMarketPriceData>> {
        return recentMarketPriceRoomCache.getAll()
    }
}