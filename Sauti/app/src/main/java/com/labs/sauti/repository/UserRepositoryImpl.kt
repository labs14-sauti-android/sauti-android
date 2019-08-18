package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.model.SignInResponse
import com.labs.sauti.model.SignUpRequest
import com.labs.sauti.model.SignUpResponse
import com.labs.sauti.model.authentication.UserData
import com.labs.sauti.sp.SessionSp
import io.reactivex.Completable
import io.reactivex.Single

class UserRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val sessionSp: SessionSp,
    private val sautiAuthorization: String
): UserRepository {
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

    override fun getSignedInUser(): Single<UserData> {
        if (sessionSp.isAccessTokenValid()) {
            val user = sessionSp.getUser()
            if (user != null) return Single.just(user)

            return sautiApiService.getCurrentUser("Bearer ${sessionSp.getAccessToken()}")
                .doOnSuccess {
                    sessionSp.setUser(it)
                }
        }

        return Single.error(Throwable())
    }
}