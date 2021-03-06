package com.labs.sauti.di.module

import com.labs.sauti.repository.TradeInfoRepository
import com.labs.sauti.view_model.TradeInfoViewModel
import dagger.Module
import dagger.Provides

@Module
class TaxCalculatorModule {

    @Provides
    fun provideTradeInfoViewModelFactory(tradeInfoRepository: TradeInfoRepository): TradeInfoViewModel.Factory {
        return TradeInfoViewModel.Factory(tradeInfoRepository)
    }
}