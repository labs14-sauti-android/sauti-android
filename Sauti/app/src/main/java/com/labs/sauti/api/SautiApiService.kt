package com.labs.sauti.api

import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.SignInResponse
import com.labs.sauti.model.SignUpRequest
import com.labs.sauti.model.SignUpResponse
import com.labs.sauti.model.authentication.UserData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.trade_info.*
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
    fun getCurrentUser(@Header("Authorization") authorization: String): Single<UserData>

    @GET("/market-price/countries")
    fun getMarketPriceCountries(): Single<MutableList<String>>

    @GET("/market-price/markets")
    fun getMarketPriceMarkets(@Query("country") country: String): Single<MutableList<String>>

    @GET("/market-price/categories")
    fun getMarketPriceCategories(
        @Query("country") country: String,
        @Query("market") market: String
    ): Single<MutableList<String>>

    @GET("/market-price/products")
    fun getMarketPriceProducts(
        @Query("country") country: String,
        @Query("market") market: String,
        @Query("category") category: String
    ): Single<MutableList<String>>

    @GET("/market-price/search")
    fun searchMarketPrice(
        @Query("country") country: String,
        @Query("market") market: String,
        @Query("category") category: String,
        @Query("product") product: String
    ): Single<MarketPriceData>

    @GET("exchange-rate/all")
    fun getExchangeRates(): Single<MutableList<ExchangeRateData>>


    @GET("/trade-info/categories")
    fun getTradeInfoCategories(
        @Query("language") language: String
    ): Single<MutableList<String>>

    @GET("/trade-info/products")
    fun getTradeInfoProducts(
        @Query("language") language: String,
        @Query("category") category: String
    ): Single<MutableList<String>>

    @GET("/trade-info/origins")
    fun getTradeInfoOrigins(
        @Query("language") language : String,
        @Query("category") category : String,
        @Query("product") product: String
    ): Single<MutableList<String>>

    @GET("/trade-info/dests")
    fun getTradeInfoDests(
        @Query("language") language : String,
        @Query("category") category : String,
        @Query("product") product: String,
        @Query("origin") origin: String
    ): Single<MutableList<String>>

    @GET("/trade-info/search/taxes")
    fun searchTradeInfoTaxes(
        @Query("language") language : String,
        @Query("category") category : String,
        @Query("product") product: String,
        @Query("origin") origin: String,
        @Query("dest") dest: String,
        @Query("value") value: Double
    ): Single<MutableList<Taxes>>

    @GET("/trade-info/search/procedures")
    fun searchTradeInfoBorderProcedures(
        @Query("language") language : String,
        @Query("category") category : String,
        @Query("product") product: String,
        @Query("origin") origin: String,
        @Query("dest") dest: String,
        @Query("value") value: Double
    ): Single<MutableList<Procedure>>

    @GET("/trade-info/search/required-documents")
    fun searchTradeInfoRequiredDocuments(
        @Query("language") language : String,
        @Query("category") category : String,
        @Query("product") product: String,
        @Query("origin") origin: String,
        @Query("dest") dest: String,
        @Query("value") value: Double
    ): Single<MutableList<RequiredDocument>>

    @GET("/trade-info/search/relevant-agencies")
    fun searchTradeInfoBorderAgencies(
        @Query("language") language : String,
        @Query("category") category : String,
        @Query("product") product: String,
        @Query("origin") origin: String,
        @Query("dest") dest: String,
        @Query("value") value: Double
    ): Single<MutableList<BorderAgency>>

    @GET("/regulated-good/countries")
    fun getRegulatedGoodsCountries(
        @Query("language") language: String
    ): Single<MutableList<String>>

    @GET("/regulated-good/search")
    fun searchRegulatedData(
        @Query("language") language: String,
        @Query("country") country: String
    ): Single<RegulatedGoodData>

}