package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.MarketPrice
import com.labs.sauti.model.User
import com.labs.sauti.sp.RecentMarketPricesSp
import com.labs.sauti.sp.SessionSp
import io.reactivex.Single

class SautiRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val sautiAuthorization: String,
    private val sessionSp: SessionSp,
    private val recentMarketPricesSp: RecentMarketPricesSp) : SautiRepository {
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

    override fun searchMarketPrice(country: String, market: String, category: String, commodity: String): Single<MarketPrice> {
        // TODO test only
        val marketPrice = MarketPrice()
        marketPrice.country = "TestCountry"
        marketPrice.market = "TestMarket"
        marketPrice.product = "TestProduct"
        marketPrice.wholesale = 555L
        marketPrice.retail = 444L
        marketPrice.currency = "UGX"
        marketPrice.date = "2019-07-13 00:00:00"

        return Single.just(marketPrice)
            .doOnSuccess {
                recentMarketPricesSp.insertRecentMarketPrice(it)
            }
    }

    override fun getRecentMarketPrices(): Single<MutableList<MarketPrice>> {
        return Single.just(recentMarketPricesSp.getRecentMarketPrices())
    }
}