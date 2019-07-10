package com.sauti.sauti.repository

import com.sauti.sauti.api.SautiApiService
import com.sauti.sauti.model.LoginResponse
import io.reactivex.Single

class SautiRepositoryImpl(private val sautiApiService: SautiApiService, private val sautiAuthorization: String) : SautiRepository {
    override fun login(username: String, password: String): Single<LoginResponse> {
        return sautiApiService.login(sautiAuthorization, "password", username, password)
    }
}