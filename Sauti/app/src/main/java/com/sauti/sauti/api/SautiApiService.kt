package com.sauti.sauti.api

import com.sauti.sauti.model.LoginResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface SautiApiService {

    @FormUrlEncoded
    @POST("oauth/token")
    fun login(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<LoginResponse>

}