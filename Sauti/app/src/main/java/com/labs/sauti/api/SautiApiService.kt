package com.labs.sauti.api

import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.authentication.SignInResponse
import com.labs.sauti.model.authentication.SignUpRequest
import com.labs.sauti.model.authentication.UserData
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.exchange_rate.FavoriteExchangeRateConversionData
import com.labs.sauti.model.market_price.FavoriteMarketPriceSearchData
import com.labs.sauti.model.trade_info.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface SautiApiService {

    @POST("/users")
    fun signUp(@Body signUpRequest: SignUpRequest): Single<Long>

    @FormUrlEncoded
    @POST("/oauth/token")
    fun signIn(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<SignInResponse>

    @GET("/oauth/revoke-token")
    fun signOut(@Header("Authorization") authorization: String): Completable

    @GET("/users/user")
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

    @POST("/favorite-market-price-searches/all")
    fun addAllFavoriteMarketPriceSearches(
        @Header("Authorization") authorization: String,
        @Body favoriteMarketPricesSearches: List<FavoriteMarketPriceSearchData>
    ): Single<MutableList<FavoriteMarketPriceSearchData>>

    @HTTP(method = "DELETE", hasBody = true, path = "/favorite-market-price-searches")
    fun deleteAllFavoriteMarketPriceSearchesById(
        @Header("Authorization") authorization: String,
        @Body favoriteMarketPricesSearchIds: List<Long>
    ): Completable

    @GET("/favorite-market-price-searches")
    fun getFavoriteMarketPriceSearches(
        @Header("Authorization") authorization: String
    ): Single<MutableList<FavoriteMarketPriceSearchData>>

    @GET("/exchange-rate/all")
    fun getExchangeRates(): Single<MutableList<ExchangeRateData>>

    @POST("/favorite-exchange-rate-conversions/all")
    fun addAllFavoriteExchangeRateConversions(
        @Header("Authorization") authorization: String,
        @Body favoriteExchangeRateConversions: MutableList<FavoriteExchangeRateConversionData>
    ): Single<MutableList<FavoriteExchangeRateConversionData>>

    @HTTP(method = "DELETE", hasBody = true, path = "/favorite-exchange-rate-conversions")
    fun deleteAllFavoriteExchangeRateConversions(
        @Header("Authorization") authorization: String,
        @Body favoriteExchangeRateConversionIds: List<Long>
    ): Completable

    @GET("/favorite-exchange-rate-conversions")
    fun getAllFavoriteExchangeRateConversions(
        @Header("Authorization") authorization: String
    ): Single<MutableList<FavoriteExchangeRateConversionData>>

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

    @GET("/regulated-good/search/prohibiteds")
    fun searchRegulatedProhibiteds(
        @Query("language") language: String,
        @Query("country") country: String
    ): Single<MutableList<Prohibited>>

    @GET("/regulated-good/search/restricteds")
    fun searchRegulatedRestricteds(
        @Query("language") language: String,
        @Query("country") country: String
    ): Single<MutableList<Restricted>>

    @GET("/regulated-good/search/sensitives")
    fun searchRegulatedSensitives(
        @Query("language") language: String,
        @Query("country") country: String
    ): Single<MutableList<Sensitive>>



}