package com.labs.sauti.di.component

import com.labs.sauti.di.module.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class, GsonModule::class])
interface MainComponent {
    fun plus(authenticationModule: AuthenticationModule): AuthenticationComponent
    fun plus(marketPricesModule: MarketPricesModule): MarketPricesComponent
}