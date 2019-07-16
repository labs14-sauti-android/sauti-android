package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.MarketPrice
import com.labs.sauti.model.MarketPriceCategory
import com.labs.sauti.model.MarketPriceCountry
import com.labs.sauti.model.MarketPriceMarket
import com.labs.sauti.repository.SautiRepository

class MarketPricesViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    private val countriesLiveData = MutableLiveData<MutableList<String>>()
    private val marketsLiveData = MutableLiveData<MutableList<String>>()
    private val categoriesLiveData = MutableLiveData<MutableList<String>>()
    private val commoditiesLiveData = MutableLiveData<MutableList<String>>()
    private val searchMarketPriceLiveData = MutableLiveData<MarketPrice>()
    private val recentMarketPricesLiveData = MutableLiveData<MutableList<MarketPrice>>()
    // TODO error
    private val errorLiveData = MutableLiveData<Throwable>()

    fun getCountriesLiveData(): LiveData<MutableList<String>> = countriesLiveData
    fun getMarketsLiveData(): LiveData<MutableList<String>> = marketsLiveData
    fun getCategoriesLiveData(): LiveData<MutableList<String>> = categoriesLiveData
    fun getCommoditiesLiveData(): LiveData<MutableList<String>> = commoditiesLiveData
    fun getSearchMarketPriceLiveData(): LiveData<MarketPrice> = searchMarketPriceLiveData
    fun getRecentMarketPricesLiveData(): LiveData<MutableList<MarketPrice>> = recentMarketPricesLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun getCountries() {
        val disposable = sautiRepository.getMarketPriceCountries()
            .map {
                return@map it.mapNotNull { mpc -> mpc.country }.toMutableList()
            }
            .subscribe(
                {
                    countriesLiveData.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            )
        addDisposable(disposable)
    }

    fun getMarkets(country: String) {
        val disposable = sautiRepository.getMarketPriceMarkets(MarketPriceCountry(country))
            .map {
                return@map it.mapNotNull { mpm -> mpm.market }.toMutableList()
            }
            .subscribe(
                {
                    marketsLiveData.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            )
        addDisposable(disposable)
    }

    fun getCategories(country: String, market: String) {
        val disposable = sautiRepository.getMarketPriceCategories(MarketPriceCountry(country), MarketPriceMarket(market))
            .map {
                return@map it.mapNotNull { mpc -> mpc.category }.toMutableList()
            }
            .subscribe(
                {
                    categoriesLiveData.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            )
        addDisposable(disposable)
    }

    fun getCommodities(country: String, market: String, category: String) {
        val disposable = sautiRepository.getMarketPriceCommodities(
            MarketPriceCountry(country), MarketPriceMarket(market), MarketPriceCategory(category))
            .map {
                return@map it.mapNotNull { mpc -> mpc.commodity }.toMutableList()
            }
            .subscribe(
                {
                    commoditiesLiveData.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            )
        addDisposable(disposable)
    }

    fun searchMarketPrice(country: String, market: String, category: String, commodity: String) {
        val disposable = sautiRepository.searchMarketPrice(country, market, category, commodity).subscribe(
            {
                searchMarketPriceLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(it)
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
                errorLiveData.postValue(it)
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