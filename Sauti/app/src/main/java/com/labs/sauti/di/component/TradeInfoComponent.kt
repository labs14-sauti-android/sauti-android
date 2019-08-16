package com.labs.sauti.di.component

import com.labs.sauti.di.module.TradeInfoModule
import com.labs.sauti.fragment.TaxCalculatorFragment
import com.labs.sauti.fragment.TaxCalculatorSearchFragment
import com.labs.sauti.fragment.TradeInfoFragment
import com.labs.sauti.fragment.TradeInfoSearchFragment
import dagger.Subcomponent

@Subcomponent(modules = [TradeInfoModule::class])
interface TradeInfoComponent {
    fun inject(tradeInfoFragment: TradeInfoFragment)
    fun inject(tradeInfoSearchFragment: TradeInfoSearchFragment)




}