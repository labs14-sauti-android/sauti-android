package com.sauti.sauti.repository

import com.sauti.sauti.model.LoginResponse
import com.sauti.sauti.model.User
import io.reactivex.Single

interface SautiRepository {

    fun login(username: String, password: String): Single<LoginResponse>
    fun getCurrentUser(accessToken: String): Single<User>

}