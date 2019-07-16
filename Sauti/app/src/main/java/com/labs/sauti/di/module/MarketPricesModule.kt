package com.labs.sauti.di.module

import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_model.MarketPricesViewModel
import dagger.Module
import dagger.Provides

@Module
class MarketPricesModule {

    @Provides
    fun provideMarketPricesViewModelFactory(sautiRepository: SautiRepository): MarketPricesViewModel.Factory {
        return MarketPricesViewModel.Factory(sautiRepository)
    }
}