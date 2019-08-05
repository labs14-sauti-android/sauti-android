package com.labs.sauti.di.module

import android.content.Context
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.*
import com.labs.sauti.db.ExchangeRateConversionDao
import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.mapper.MarketPriceDataRecentMarketPriceDataMapper
import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.repository.SautiRepositoryImpl
import com.labs.sauti.sp.SessionSp
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
    fun provideSautiRoomDatabase(context: Context) : SautiRoomDatabase{
        return SautiRoomDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideMarketPriceRoomCache(sautiRoomDatabase: SautiRoomDatabase): MarketPriceRoomCache {
        return MarketPriceRoomCache(sautiRoomDatabase)
    }

    // TODO remove RecentMarketPrice
    @Provides
    @Singleton
    fun provideRecentMarketPriceRoomCache(sautiRoomDatabase: SautiRoomDatabase): RecentMarketPriceRoomCache {
        return RecentMarketPriceRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideRecentMarketPriceSearchRoomCache(sautiRoomDatabase: SautiRoomDatabase): RecentMarketPriceSearchRoomCache {
        return RecentMarketPriceSearchRoomCache(sautiRoomDatabase)
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
        marketPriceRoomCache: MarketPriceRoomCache,
        recentMarketPriceRoomCache: RecentMarketPriceRoomCache, // TODO remove
        recentMarketPriceSearchRoomCache: RecentMarketPriceSearchRoomCache,
        marketPriceDataRecentMarketPriceDataMapper: MarketPriceDataRecentMarketPriceDataMapper,
        exchangeRateRoomCache: ExchangeRateRoomCache,
        exchangeRateConversionRoomCache: ExchangeRateConversionRoomCache
    ): SautiRepository {
        return SautiRepositoryImpl(
            networkHelper,
            sautiApiService,
            sautiAuthorization,
            sessionSp,
            marketPriceRoomCache,
            recentMarketPriceRoomCache, // TODO remove
            recentMarketPriceSearchRoomCache,
            marketPriceDataRecentMarketPriceDataMapper,
            exchangeRateRoomCache,
            exchangeRateConversionRoomCache
        )
    }

}