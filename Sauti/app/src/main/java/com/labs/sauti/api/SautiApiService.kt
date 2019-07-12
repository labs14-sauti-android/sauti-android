package com.labs.sauti.api

import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.User
import io.reactivex.Single
import retrofit2.http.*

interface SautiApiService {

    @FormUrlEncoded
    @POST("oauth/token")
    fun login(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<LoginResponse>

    @GET("users/getusername")
    fun getCurrentUser(@Header("Authorization") authorization: String): Single<User>

}