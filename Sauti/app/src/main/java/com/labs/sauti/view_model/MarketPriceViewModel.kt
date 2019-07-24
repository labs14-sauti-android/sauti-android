package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.*
import com.labs.sauti.repository.SautiRepository

class MarketPriceViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    // TODO make these lazy
    private val countriesLiveData = MutableLiveData<MutableList<String>>()
    private val marketsLiveData = MutableLiveData<MutableList<String>>()
    private val categoriesLiveData = MutableLiveData<MutableList<String>>()
    private val productsLiveData = MutableLiveData<MutableList<String>>()
    private val searchMarketPriceLiveData = MutableLiveData<MarketPriceData>()
    private val recentMarketPricesLiveData = MutableLiveData<MutableList<RecentMarketPriceData>>()
    private val recentMarketPriceSearchesLiveData = MutableLiveData<MutableList<RecentMarketPriceSearchData>>()
    private val searchRecentMarketPricesLiveData = MutableLiveData<MutableList<MarketPriceData>>()
    // TODO error
    private val errorLiveData = MutableLiveData<Throwable>()

    fun getCountriesLiveData(): LiveData<MutableList<String>> = countriesLiveData
    fun getMarketsLiveData(): LiveData<MutableList<String>> = marketsLiveData
    fun getCategoriesLiveData(): LiveData<MutableList<String>> = categoriesLiveData
    fun getProductsLiveData(): LiveData<MutableList<String>> = productsLiveData
    fun getSearchMarketPriceLiveData(): LiveData<MarketPriceData> = searchMarketPriceLiveData
    fun getRecentMarketPricesLiveData(): LiveData<MutableList<RecentMarketPriceData>> = recentMarketPricesLiveData
    fun getRecentMarketPriceSearchesLiveData(): LiveData<MutableList<RecentMarketPriceSearchData>> = recentMarketPriceSearchesLiveData
    fun getSearchRecentMarketPricesLiveData(): LiveData<MutableList<MarketPriceData>> = searchRecentMarketPricesLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun getCountries() {
        val disposable = sautiRepository.getMarketPriceCountries()
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
        val disposable = sautiRepository.getMarketPriceMarkets(country)
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
        val disposable = sautiRepository.getMarketPriceCategories(country, market)
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

    fun getProducts(country: String, market: String, category: String) {
        val disposable = sautiRepository.getMarketPriceProducts(country, market, category)
            .subscribe(
                {
                    productsLiveData.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            )
        addDisposable(disposable)
    }

    fun searchMarketPrice(country: String, market: String, category: String, product: String) {
        val disposable = sautiRepository.searchMarketPrice(country, market, category, product).subscribe(
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

    fun getRecentMarketPriceSearches() {
        addDisposable(sautiRepository.getRecentMarketPriceSearches().subscribe(
            {
                recentMarketPriceSearchesLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun searchRecentMarketPrices() {
        addDisposable(sautiRepository.searchRecentMarketPrices().subscribe(
            {
                searchRecentMarketPricesLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun searchRecentMarketPriceInCache() {
        addDisposable(sautiRepository.searchRecentMarketPriceInCache().subscribe(
            {
                searchRecentMarketPricesLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    class Factory(private val sautiRepository: SautiRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MarketPriceViewModel(sautiRepository) as T
        }
    }

}