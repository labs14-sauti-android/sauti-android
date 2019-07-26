package com.labs.sauti.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.repository.SautiRepository


class TradeInfoViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    class Factory(private val sautiRepository: SautiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AuthenticationViewModel(sautiRepository) as T
        }
    }
}