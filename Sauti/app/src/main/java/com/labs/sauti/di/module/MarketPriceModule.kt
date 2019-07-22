package com.labs.sauti.di.module

import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_model.MarketPriceViewModel
import dagger.Module
import dagger.Provides

@Module
class MarketPriceModule {

    @Provides
    fun provideMarketPriceViewModelFactory(sautiRepository: SautiRepository): MarketPriceViewModel.Factory {
        return MarketPriceViewModel.Factory(sautiRepository)
    }
}