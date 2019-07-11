package com.sauti.sauti.di.module

import android.content.Context
import com.sauti.sauti.api.SautiApiService
import com.sauti.sauti.repository.SautiRepository
import com.sauti.sauti.repository.SautiRepositoryImpl
import com.sauti.sauti.sp.SessionSp
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
    fun provideAniSearchRepository(sautiApiService: SautiApiService): SautiRepository {
        return SautiRepositoryImpl(sautiApiService, sautiAuthorization)
    }

}