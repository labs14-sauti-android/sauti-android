package com.labs.sauti.repository

import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.User
import io.reactivex.Single

interface SautiRepository {

    fun login(username: String, password: String): Single<LoginResponse>
    fun signOut(): Single<Unit>
    fun isAccessTokenValid(): Single<Boolean>
    fun getCurrentUser(): Single<User>

}