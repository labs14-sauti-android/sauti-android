package com.labs.sauti.api

import com.labs.sauti.model.SignInResponse
import com.labs.sauti.model.SignUpRequest
import com.labs.sauti.model.SignUpResponse
import com.labs.sauti.model.User
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface SautiApiService {

    @POST("/users/user")
    fun signUp(@Body signUpRequest: SignUpRequest): Single<SignUpResponse>

    @FormUrlEncoded
    @POST("oauth/token")
    fun signIn(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<SignInResponse>

    @GET("/oauth/revoke-token")
    fun signOut(@Header("Authorization") authorization: String): Completable

    @GET("users/getusername")
    fun getCurrentUser(@Header("Authorization") authorization: String): Single<User>

    @GET("exchange-rate/all")
    fun getExchangeRates(): Single<MutableList<ExchangeRateData>>
}