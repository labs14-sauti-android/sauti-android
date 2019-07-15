package com.labs.sauti.di.component

import com.labs.sauti.activity.MarketPricesActivity
import com.labs.sauti.di.module.MarketPricesModule
import dagger.Subcomponent

@Subcomponent(modules = [MarketPricesModule::class])
interface MarketPricesComponent {
    fun inject(marketPricesActivity: MarketPricesActivity)
}