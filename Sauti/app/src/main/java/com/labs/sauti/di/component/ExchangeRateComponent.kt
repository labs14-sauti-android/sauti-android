package com.labs.sauti.di.component

import com.labs.sauti.di.module.ExchangeRateModule
import com.labs.sauti.fragment.ExchangeRateFragment
import com.labs.sauti.fragment.ExchangeRateSearchFragment
import dagger.Subcomponent

@Subcomponent(modules = [ExchangeRateModule::class])
interface ExchangeRateComponent {
    fun inject(exchangeRateFragment: ExchangeRateFragment)
    fun inject(exchangeRateSearchFragment: ExchangeRateSearchFragment)
}