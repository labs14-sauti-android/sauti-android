package com.labs.sauti.di.module

import android.content.Context
import com.labs.sauti.helper.NetworkHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideAppContext(): Context {
        return context.applicationContext
    }

}