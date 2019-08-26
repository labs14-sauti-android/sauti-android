package com.labs.sauti.di.module

import com.labs.sauti.repository.ExchangeRateRepository
import com.labs.sauti.repository.MarketPriceRepository
import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_model.DashboardFavoritesViewModel
import dagger.Module
import dagger.Provides

@Module
class DashboardModule {

    @Provides
    fun provideDashboardFavoritesViewModelFactory(
        userRepository: UserRepository,
        marketPriceRepository: MarketPriceRepository,
        exchangeRateRepository: ExchangeRateRepository
    ): DashboardFavoritesViewModel.Factory {
        return DashboardFavoritesViewModel.Factory(
            userRepository,
            marketPriceRepository,
            exchangeRateRepository
        )
    }
}