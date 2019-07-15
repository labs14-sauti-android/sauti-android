package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.User
import com.labs.sauti.sp.SessionSp
import io.reactivex.Single

class SautiRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val sautiAuthorization: String,
    private val sessionSp: SessionSp) : SautiRepository {
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

        return Single.fromCallable {
            throw Throwable("Not logged in")
        }
    }
}