package com.labs.sauti.di.module

import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_model.ExchangeRateViewModel
import dagger.Module
import dagger.Provides

@Module
class ExchangeRateModule {

    @Provides
    fun provideExchangeRateViewModelFactory(sautiRepository: SautiRepository): ExchangeRateViewModel.Factory {
        return ExchangeRateViewModel.Factory(sautiRepository)
    }

}