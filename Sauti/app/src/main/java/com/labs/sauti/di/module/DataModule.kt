package com.labs.sauti.di.module

import android.content.Context
import com.google.gson.Gson
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceRoomCache
import com.labs.sauti.cache.RecentMarketPriceRoomCache
import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.mapper.MarketPriceDataRecentMarketPriceDataMapper
import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.repository.SautiRepositoryImpl
import com.labs.sauti.sp.RecentMarketPricesSp
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

    @Provides
    @Singleton
    fun provideRecentMarketPriceRoomCache(sautiRoomDatabase: SautiRoomDatabase): RecentMarketPriceRoomCache {
        return RecentMarketPriceRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideSautiRepository(
        sautiApiService: SautiApiService,
        sessionSp: SessionSp,
        marketPriceRoomCache: MarketPriceRoomCache,
        recentMarketPriceRoomCache: RecentMarketPriceRoomCache,
        marketPriceDataRecentMarketPriceDataMapper: MarketPriceDataRecentMarketPriceDataMapper
    ): SautiRepository {
        return SautiRepositoryImpl(
            sautiApiService,
            sautiAuthorization,
            sessionSp,
            marketPriceRoomCache,
            recentMarketPriceRoomCache,
            marketPriceDataRecentMarketPriceDataMapper
        )
    }

}