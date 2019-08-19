package com.labs.sauti.di.module

import com.labs.sauti.repository.ExchangeRateRepository
import com.labs.sauti.view_model.ExchangeRateViewModel
import dagger.Module
import dagger.Provides

@Module
class ExchangeRateModule {

    @Provides
    fun provideExchangeRateViewModelFactory(exchangeRateRepository: ExchangeRateRepository): ExchangeRateViewModel.Factory {
        return ExchangeRateViewModel.Factory(exchangeRateRepository)
    }

}