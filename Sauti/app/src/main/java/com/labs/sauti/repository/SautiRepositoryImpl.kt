package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.User
import io.reactivex.Single

class SautiRepositoryImpl(private val sautiApiService: SautiApiService, private val sautiAuthorization: String) : SautiRepository {
    override fun login(username: String, password: String): Single<LoginResponse> {
        return sautiApiService.login(sautiAuthorization, "password", username, password)
    }

    override fun getCurrentUser(accessToken: String): Single<User> {
        return sautiApiService.getCurrentUser("Bearer $accessToken")
    }
}