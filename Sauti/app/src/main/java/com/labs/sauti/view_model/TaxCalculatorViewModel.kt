package com.labs.sauti.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.repository.TradeInfoRepository

class TaxCalculatorViewModel(private val sautiRepository: SautiRepository) : BaseViewModel() {

    class Factory(private val tradeInfoRepository: TradeInfoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TradeInfoViewModel(tradeInfoRepository) as T
        }
    }
}