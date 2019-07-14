package com.labs.sauti.di.component

import com.labs.sauti.di.module.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class, GsonModule::class])
interface MainComponent {
    fun plus(loginModule: LoginModule): LoginComponent
    fun plus(userModule: UserModule): UserComponent
    fun plus(marketPricesModule: MarketPricesModule): MarketPricesComponent
    //fun plus(marketPricesModule: MarketPricesModule): MarketPricesComponent
}