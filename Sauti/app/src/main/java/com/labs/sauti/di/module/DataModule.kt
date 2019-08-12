package com.labs.sauti.di.module

import android.content.Context
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceRoomCache
import com.labs.sauti.cache.MarketPriceSearchRoomCache
import com.labs.sauti.cache.*
import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.repository.HelpRepository
import com.labs.sauti.repository.HelpRepositoryImpl
import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.repository.SautiRepositoryImpl
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
    fun provideHelpRepository(sautiApiService: SautiApiService): HelpRepository {
        return HelpRepositoryImpl(sautiApiService)
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

}