package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_state.market_price.*

class MarketPriceViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<Throwable>() }
    private val countriesViewState by lazy { MutableLiveData<CountriesViewState>() }
    private val marketsViewState by lazy { MutableLiveData<MarketsViewState>() }
    private val categoriesViewState by lazy { MutableLiveData<CategoriesViewState>() }
    private val productsViewState by lazy { MutableLiveData<ProductsViewState>() }
    private val searchMarketPriceLiveData by lazy { MutableLiveData<MarketPrice>() }
    private val recentMarketPricesViewState by lazy { MutableLiveData<RecentMarketPricesViewState>() }

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getCountriesViewState(): LiveData<CountriesViewState> = countriesViewState
    fun getMarketsViewState(): LiveData<MarketsViewState> = marketsViewState
    fun getCategoriesViewState(): LiveData<CategoriesViewState> = categoriesViewState
    fun getProductsViewState(): LiveData<ProductsViewState> = productsViewState
    fun getSearchMarketPriceLiveData(): LiveData<MarketPrice> = searchMarketPriceLiveData
    fun getRecentMarketPricesViewState(): LiveData<RecentMarketPricesViewState> = recentMarketPricesViewState

    fun getCountries() {
        countriesViewState.value = CountriesViewState(true)
        addDisposable(sautiRepository.getMarketPriceCountries().subscribe(
            {
                countriesViewState.postValue(CountriesViewState(false, it))
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun getMarkets(country: String) {
        marketsViewState.value = MarketsViewState(true)
        addDisposable(sautiRepository.getMarketPriceMarkets(country).subscribe(
            {
                marketsViewState.postValue(MarketsViewState(false, it))
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun getCategories(country: String, market: String) {
        categoriesViewState.value = CategoriesViewState(true)
        addDisposable(sautiRepository.getMarketPriceCategories(country, market).subscribe(
            {
                categoriesViewState.postValue(CategoriesViewState(false, it))
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun getProducts(country: String, market: String, category: String) {
        productsViewState.value = ProductsViewState(true)
        addDisposable(sautiRepository.getMarketPriceProducts(country, market, category).subscribe(
            {
                    productsViewState.postValue(ProductsViewState(false, it))
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun searchMarketPrice(country: String, market: String, category: String, product: String) {
        addDisposable(sautiRepository.searchMarketPrice(country, market, category, product)
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
            ))
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