package com.labs.sauti.di.component

import com.labs.sauti.activity.MarketPricesActivity
import com.labs.sauti.di.module.MarketPricesModule
import com.labs.sauti.fragment.MarketPriceFragment
import com.labs.sauti.fragment.MarketPriceSearchFragment
import dagger.Subcomponent

@Subcomponent(modules = [MarketPricesModule::class])
interface MarketPricesComponent {
    fun inject(marketPricesActivity: MarketPricesActivity)
    fun inject(marketPriceFragment: MarketPriceFragment)
    fun inject(marketPriceSearchFragment: MarketPriceSearchFragment)
}