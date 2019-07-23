package com.labs.sauti.di.component

import com.labs.sauti.di.module.MarketPriceModule
import com.labs.sauti.fragment.MarketPriceFragment
import com.labs.sauti.fragment.MarketPriceSearchFragment
import dagger.Subcomponent

@Subcomponent(modules = [MarketPriceModule::class])
interface MarketPriceComponent {
    fun inject(marketPriceFragment: MarketPriceFragment)
    fun inject(marketPriceSearchFragment: MarketPriceSearchFragment)
}