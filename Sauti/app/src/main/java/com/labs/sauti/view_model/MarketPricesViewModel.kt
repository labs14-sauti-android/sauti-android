package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.MarketPrice
import com.labs.sauti.repository.SautiRepository

class MarketPricesViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    private val searchMarketPriceLiveData = MutableLiveData<MarketPrice>()
    private val recentMarketPricesLiveData = MutableLiveData<MutableList<MarketPrice>>()

    fun getSearchMarketPriceLiveData(): LiveData<MarketPrice> = searchMarketPriceLiveData
    fun getRecentMarketPricesLiveData(): LiveData<MutableList<MarketPrice>> = recentMarketPricesLiveData

    fun searchMarketPrice(country: String, market: String, category: String, commodity: String) {
        val disposable = sautiRepository.searchMarketPrice(country, market, category, commodity).subscribe(
            {
                searchMarketPriceLiveData.postValue(it)
            },
            {
                // TODO
            }
        )
        addDisposable(disposable)
    }

    fun getRecentMarketPrices() {
        val disposable = sautiRepository.getRecentMarketPrices().subscribe(
            {
                recentMarketPricesLiveData.postValue(it)
            },
            {
                // TODO
            }
        )
        addDisposable(disposable)
    }

    class Factory(private val sautiRepository: SautiRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MarketPricesViewModel(sautiRepository) as T
        }
    }

}