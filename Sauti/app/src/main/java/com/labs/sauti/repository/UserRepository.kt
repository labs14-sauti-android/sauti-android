package com.labs.sauti.repository

import com.labs.sauti.model.authentication.SignInResponse
import com.labs.sauti.model.authentication.SignUpRequest
import com.labs.sauti.model.authentication.UserData
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun signUp(signUpRequest: SignUpRequest): Single<Long>
    fun signIn(username: String, password: String): Single<SignInResponse>
    fun signOut(): Completable
    fun isAccessTokenValid(): Single<Boolean>
    fun getSignedInUser(): Single<UserData>
}