package com.labs.sauti.repository

import com.labs.sauti.model.SignInResponse
import com.labs.sauti.model.SignUpRequest
import com.labs.sauti.model.SignUpResponse
import com.labs.sauti.model.authentication.UserData
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun signUp(signUpRequest: SignUpRequest): Single<SignUpResponse>
    fun signIn(username: String, password: String): Single<SignInResponse>
    fun signOut(): Completable
    fun isAccessTokenValid(): Single<Boolean>
    fun getSignedInUser(): Single<UserData?>
}