package com.labs.sauti.di.module

import android.content.Context
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.MarketPriceRoomCache
import com.labs.sauti.cache.MarketPriceSearchRoomCache
import com.labs.sauti.cache.*
import com.labs.sauti.db.SautiRoomDatabase
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
    fun provideTradeInfoRoomCache(sautiRoomDatabase: SautiRoomDatabase) : TradeInfoRoomCache {
        return TradeInfoRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideTradeInfoSearchRoomCache(sautiRoomDatabase: SautiRoomDatabase): TradeInfoSearchRoomCache {
        return TradeInfoSearchRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideHelpRepository(sautiApiService: SautiApiService): HelpRepository {
        return HelpRepositoryImpl(sautiApiService)
    }

    @Provides
    @Singleton
    fun provideReportRepository(sautiApiService: SautiApiService): ReportRepository {
        return ReportRepositoryImpl(sautiApiService)
    }

    @Provides
    @Singleton
    fun provideFavoriteMarketPriceRoomCache(sautiRoomDatabase: SautiRoomDatabase): FavoriteMarketPriceSearchRoomCache {
        return FavoriteMarketPriceSearchRoomCache(sautiRoomDatabase)
    }

    @Provides
    @Singleton
    fun provideMarketPriceRepository(
        sautiApiService: SautiApiService,
        marketPriceRoomCache: MarketPriceRoomCache,
        marketPriceSearchRoomCache: MarketPriceSearchRoomCache,
        sessionSp: SessionSp,
        favoriteMarketPriceSearchRoomCache: FavoriteMarketPriceSearchRoomCache
    ): MarketPriceRepository {
        return MarketPriceRepositoryImpl(
            sautiApiService,
            marketPriceRoomCache,
            marketPriceSearchRoomCache,
            favoriteMarketPriceSearchRoomCache,
            sessionSp
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

    @Provides
    @Singleton
    fun provideTradeInfoRepository(
        sautiApiService: SautiApiService,
        settingsSp: SettingsSp,
        tradeInfoRoomCache: TradeInfoRoomCache,
        tradeInfoSearchRoomCache: TradeInfoSearchRoomCache
    ) : TradeInfoRepository {
        return TradeInfoRepositoryImpl(sautiApiService,
            settingsSp,
            tradeInfoRoomCache,
            tradeInfoSearchRoomCache)
    }
}