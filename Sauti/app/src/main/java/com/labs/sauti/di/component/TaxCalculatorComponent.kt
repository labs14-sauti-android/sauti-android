package com.labs.sauti.di.component

import com.labs.sauti.di.module.TaxCalculatorModule
import com.labs.sauti.fragment.TaxCalculatorFragment
import com.labs.sauti.fragment.TaxCalculatorSearchFragment
import dagger.Subcomponent

@Subcomponent(modules = [TaxCalculatorModule::class])
interface TaxCalculatorComponent {

    fun inject(taxCalculatorFragment: TaxCalculatorFragment)
    fun inject(taxCalculatorSearchFragment: TaxCalculatorSearchFragment)
}