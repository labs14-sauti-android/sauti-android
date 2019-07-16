package com.labs.sauti.di.module

import android.content.Context
import com.google.gson.Gson
import com.labs.sauti.api.SautiApiService
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
    fun provideRecentMarketPricesSp(context: Context, gson: Gson): RecentMarketPricesSp {
        return RecentMarketPricesSp(context, gson)
    }

    @Provides
    @Singleton
    fun provideSautiRepository(sautiApiService: SautiApiService, sessionSp: SessionSp, recentMarketPricesSp: RecentMarketPricesSp): SautiRepository {
        return SautiRepositoryImpl(sautiApiService, sautiAuthorization, sessionSp, recentMarketPricesSp)
    }

}