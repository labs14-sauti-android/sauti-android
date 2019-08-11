package com.labs.sauti.di.module

import android.content.Context
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceRoomCache
import com.labs.sauti.cache.MarketPriceSearchRoomCache
import com.labs.sauti.cache.*
import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.repository.*
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.sp.SettingsSp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(private val sautiAuthorization: String) {

    @Provides
    @Singleton
    fun provideSessionSp(context: Context): SessionSp {
        return SessionSp(context)
    }

    @Provides
    @Singleton
    fun provideSettingsSp(context: Context): SettingsSp {
        return SettingsSp(context)
    }

    @Provides
    @Singleton
    fun provideSautiRoomDatabase(context: Context) : SautiRoomDatabase{
        return SautiRoomDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideMarketPriceRoomCache(sautiRoomDatabase: SautiRoomDatabase): MarketPriceRoomCache {
        return MarketPriceRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideMarketPriceSearchRoomCache(sautiRoomDatabase: SautiRoomDatabase): MarketPriceSearchRoomCache {
        return MarketPriceSearchRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideExchangeRateRoomCache(sautiRoomDatabase: SautiRoomDatabase): ExchangeRateRoomCache {
        return ExchangeRateRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideExchangeRateConversionRoomCache(sautiRoomDatabase: SautiRoomDatabase): ExchangeRateConversionRoomCache {
        return ExchangeRateConversionRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideSautiRepository(
        networkHelper: NetworkHelper,
        sautiApiService: SautiApiService,
        sessionSp: SessionSp,
        settingsSp: SettingsSp,
        marketPriceRoomCache: MarketPriceRoomCache,
        marketPriceSearchRoomCache: MarketPriceSearchRoomCache,
        exchangeRateRoomCache: ExchangeRateRoomCache,
        exchangeRateConversionRoomCache: ExchangeRateConversionRoomCache
    ): SautiRepository {
        return SautiRepositoryImpl(
            networkHelper,
            sautiApiService,
            sautiAuthorization,
            sessionSp,
            settingsSp,
            marketPriceRoomCache,
            marketPriceSearchRoomCache,
            exchangeRateRoomCache,
            exchangeRateConversionRoomCache
        )
    }

    @Provides
    @Singleton
    fun provideMarketPriceRepository(
        sautiApiService: SautiApiService,
        marketPriceRoomCache: MarketPriceRoomCache,
        marketPriceSearchRoomCache: MarketPriceSearchRoomCache
    ): MarketPriceRepository {
        return MarketPriceRepositoryImpl(
            sautiApiService,
            marketPriceRoomCache,
            marketPriceSearchRoomCache
        )
    }

    @Provides
    @Singleton
    fun provideExchangeRateRepository(
        sautiApiService: SautiApiService,
        exchangeRateRoomCache: ExchangeRateRoomCache,
        exchangeRateConversionRoomCache: ExchangeRateConversionRoomCache
    ): ExchangeRateRepository {
        return ExchangeRateRepositoryImpl(
            sautiApiService,
            exchangeRateRoomCache,
            exchangeRateConversionRoomCache
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        sautiApiService: SautiApiService,
        sessionSp: SessionSp
    ): UserRepository {
        return UserRepositoryImpl(
            sautiApiService,
            sessionSp,
            sautiAuthorization
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsSp: SettingsSp): SettingsRepository {
        return SettingsRepositoryImpl(settingsSp)
    }

}