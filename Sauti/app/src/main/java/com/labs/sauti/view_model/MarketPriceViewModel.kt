package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_state.market_price.RecentMarketPricesViewState

class MarketPriceViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    // TODO make these lazy
    private val countriesLiveData = MutableLiveData<MutableList<String>>()
    private val marketsLiveData = MutableLiveData<MutableList<String>>()
    private val categoriesLiveData = MutableLiveData<MutableList<String>>()
    private val productsLiveData = MutableLiveData<MutableList<String>>()
    private val searchMarketPriceLiveData = MutableLiveData<MarketPrice>()
    private val recentMarketPricesViewState by lazy { MutableLiveData<RecentMarketPricesViewState>() }
    private val errorLiveData = MutableLiveData<Throwable>()

    fun getCountriesLiveData(): LiveData<MutableList<String>> = countriesLiveData
    fun getMarketsLiveData(): LiveData<MutableList<String>> = marketsLiveData
    fun getCategoriesLiveData(): LiveData<MutableList<String>> = categoriesLiveData
    fun getProductsLiveData(): LiveData<MutableList<String>> = productsLiveData
    fun getSearchMarketPriceLiveData(): LiveData<MarketPrice> = searchMarketPriceLiveData
    fun getRecentMarketPricesViewState(): LiveData<RecentMarketPricesViewState> = recentMarketPricesViewState

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
        val disposable = sautiRepository.searchMarketPrice(country, market, category, product)
            .map { marketPriceData ->
                MarketPrice(
                    marketPriceData.country,
                    marketPriceData.market,
                    marketPriceData.productAgg,
                    marketPriceData.productCat,
                    marketPriceData.product,
                    marketPriceData.wholesale,
                    marketPriceData.retail,
                    marketPriceData.currency,
                    marketPriceData.date
                )
            }
            .subscribe(
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
        recentMarketPricesViewState.value = RecentMarketPricesViewState(true)
        addDisposable(sautiRepository.searchRecentMarketPrices()
            .map {
                it.map { marketPriceData ->
                    MarketPrice(
                        marketPriceData.country,
                        marketPriceData.market,
                        marketPriceData.productAgg,
                        marketPriceData.productCat,
                        marketPriceData.product,
                        marketPriceData.wholesale,
                        marketPriceData.retail,
                        marketPriceData.currency,
                        marketPriceData.date
                    )
                }
            }
            .subscribe(
            {
                recentMarketPricesViewState.postValue(RecentMarketPricesViewState(
                    false,
                    it
                ))
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun getRecentMarketPricesInCache() {
        recentMarketPricesViewState.value = RecentMarketPricesViewState(true)
        addDisposable(sautiRepository.searchRecentMarketPriceInCache()
            .map {
                it.map { marketPriceData ->
                    MarketPrice(
                        marketPriceData.country,
                        marketPriceData.market,
                        marketPriceData.productAgg,
                        marketPriceData.productCat,
                        marketPriceData.product,
                        marketPriceData.wholesale,
                        marketPriceData.retail,
                        marketPriceData.currency,
                        marketPriceData.date
                    )
                }
            }
            .subscribe(
            {
                recentMarketPricesViewState.postValue(RecentMarketPricesViewState(
                    false,
                    it
                ))
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