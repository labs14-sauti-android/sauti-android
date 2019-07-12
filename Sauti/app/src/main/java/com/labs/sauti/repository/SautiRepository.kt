package com.labs.sauti.repository

import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.User
import io.reactivex.Single

interface SautiRepository {

    fun login(username: String, password: String): Single<LoginResponse>
    fun getCurrentUser(accessToken: String): Single<User>

}