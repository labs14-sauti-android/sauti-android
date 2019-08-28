package com.labs.sauti.di.module

import com.labs.sauti.repository.ExchangeRateRepository
import com.labs.sauti.repository.MarketPriceRepository
import com.labs.sauti.repository.TradeInfoRepository
import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_model.DashboardFavoritesViewModel
import com.labs.sauti.view_model.DashboardRecentSearchesViewModel
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

    @Provides
    fun provideDashboardRecentFavoriteViewModelFactory(
        marketPriceRepository: MarketPriceRepository,
        exchangeRateRepository: ExchangeRateRepository,
        tradeInfoRepository: TradeInfoRepository
    ): DashboardRecentSearchesViewModel.Factory {
        return DashboardRecentSearchesViewModel.Factory(
            marketPriceRepository,
            exchangeRateRepository,
            tradeInfoRepository
        )
    }
}