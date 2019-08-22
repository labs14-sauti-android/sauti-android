package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.model.authentication.SignInResponse
import com.labs.sauti.model.authentication.SignUpRequest
import com.labs.sauti.model.authentication.UserData
import com.labs.sauti.sp.SessionSp
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UserRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val sessionSp: SessionSp,
    private val sautiAuthorization: String
): UserRepository {
    override fun signUp(signUpRequest: SignUpRequest): Single<Long> {
        return sautiApiService.signUp(signUpRequest)
    }

    override fun signIn(username: String, password: String): Single<SignInResponse> {
        return sautiApiService.signIn(sautiAuthorization, "password", username, password)
            .doOnSuccess {
                sessionSp.setAccessToken(it.accessToken ?: "")
                sessionSp.setExpiresIn(it.expiresIn ?: 0)
                sessionSp.setLoggedInAt(System.currentTimeMillis() / 1000L)

                val userData = sautiApiService.getCurrentUser("Bearer ${it.accessToken}").blockingGet()
                sessionSp.setUser(userData)
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

    override fun getSignedInUser(shouldGetFromServer: Boolean): Single<UserData> {
        if (!sessionSp.isAccessTokenValid()) return Single.just(UserData()) // userId null

        if (shouldGetFromServer) {
            return sautiApiService.getCurrentUser("Bearer ${sessionSp.getAccessToken()}")
                .doOnSuccess {
                    sessionSp.setUser(it)
                }.onErrorResumeNext { // token expired in the server
                    Single.just(UserData()) // userId null
                }
                .subscribeOn(Schedulers.io())
        }

        val user = sessionSp.getUser()
        user ?: return Single.just(UserData()) // userId null

        return Single.just(user)
    }
}