package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.authentication.User
import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.repository.MarketPriceRepository
import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_state.authentication.SignedInUserViewState
import com.labs.sauti.view_state.market_price.*
import io.reactivex.Completable

class MarketPriceViewModel(
    private val marketPriceRepository: MarketPriceRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<String>() }
    private val countriesViewState by lazy { MutableLiveData<CountriesViewState>() }
    private val marketsViewState by lazy { MutableLiveData<MarketsViewState>() }
    private val categoriesViewState by lazy { MutableLiveData<CategoriesViewState>() }
    private val productsViewState by lazy { MutableLiveData<ProductsViewState>() }
    private val searchMarketPriceViewState by lazy { MutableLiveData<SearchMarketPriceViewState>() }
    private val recentMarketPricesViewState by lazy { MutableLiveData<RecentMarketPricesViewState>() }

    private val signedInUserViewState by lazy { MutableLiveData<SignedInUserViewState>() }
    private val isFavoriteMarketPriceSearchViewState by lazy { MutableLiveData<IsFavoriteMarketPriceSearchViewState>() }

    fun getErrorLiveData(): LiveData<String> = errorLiveData
    fun getCountriesViewState(): LiveData<CountriesViewState> = countriesViewState
    fun getMarketsViewState(): LiveData<MarketsViewState> = marketsViewState
    fun getCategoriesViewState(): LiveData<CategoriesViewState> = categoriesViewState
    fun getProductsViewState(): LiveData<ProductsViewState> = productsViewState
    fun getSearchMarketPriceViewState(): LiveData<SearchMarketPriceViewState> = searchMarketPriceViewState
    fun getRecentMarketPricesViewState(): LiveData<RecentMarketPricesViewState> = recentMarketPricesViewState

    fun getSignedInUserViewState(): LiveData<SignedInUserViewState> = signedInUserViewState
    fun getIsFavoriteMarketPriceSearchViewState(): LiveData<IsFavoriteMarketPriceSearchViewState> = isFavoriteMarketPriceSearchViewState

    fun updateMarketPrices() {
        addDisposable(marketPriceRepository.updateMarketPrices().subscribe(
            {

            },
            {
                errorLiveData.postValue("An error has occurred")
            }
        ))
    }

    fun getCountries() {
        countriesViewState.value = CountriesViewState(true)
        addDisposable(marketPriceRepository.getMarketPriceCountries().subscribe(
            {
                countriesViewState.postValue(CountriesViewState(false, it))
            },
            {
                countriesViewState.postValue(CountriesViewState(false))
                errorLiveData.postValue("An error has occurred")
            }
        ))
    }

    fun getMarkets(country: String) {
        marketsViewState.value = MarketsViewState(true)
        addDisposable(marketPriceRepository.getMarketPriceMarkets(country).subscribe(
            {
                marketsViewState.postValue(MarketsViewState(false, it))
            },
            {
                marketsViewState.postValue(MarketsViewState(false))
                errorLiveData.postValue("An error has occurred")
            }
        ))
    }

    fun getCategories(country: String, market: String) {
        categoriesViewState.value = CategoriesViewState(true)
        addDisposable(marketPriceRepository.getMarketPriceCategories(country, market).subscribe(
            {
                categoriesViewState.postValue(CategoriesViewState(false, it))
            },
            {
                marketsViewState.postValue(MarketsViewState(false))
                errorLiveData.postValue("An error has occurred")
            }
        ))
    }

    fun getProducts(country: String, market: String, category: String) {
        productsViewState.value = ProductsViewState(true)
        addDisposable(marketPriceRepository.getMarketPriceProducts(country, market, category).subscribe(
            {
                productsViewState.postValue(ProductsViewState(false, it))
            },
            {
                marketsViewState.postValue(MarketsViewState(false))
                errorLiveData.postValue("An error has occurred")
            }
        ))
    }

    fun searchMarketPrice(country: String, market: String, category: String, product: String) {
        searchMarketPriceViewState.value = SearchMarketPriceViewState(isLoading = true)
        addDisposable(marketPriceRepository.searchMarketPrice(true, country, market, category, product)
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
                    marketPriceData.date,
                    marketPriceData.nearbyMarketplaceNames
                )
            }
            .subscribe(
                {
                    searchMarketPriceViewState.postValue(SearchMarketPriceViewState(isLoading = false, marketPrice = it))
                },
                {
                    searchMarketPriceViewState.postValue(SearchMarketPriceViewState(isLoading = false))
                    errorLiveData.postValue("Cannot find market price")
                }
            ))
    }

    fun getRecentMarketPrices() {
        recentMarketPricesViewState.value = RecentMarketPricesViewState(true)
        addDisposable(marketPriceRepository.searchRecentMarketPrices()
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
                        marketPriceData.date,
                        marketPriceData.nearbyMarketplaceNames
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
                    recentMarketPricesViewState.postValue(RecentMarketPricesViewState(isLoading = false))
                    errorLiveData.postValue("An error has occurred")
                }
            ))
    }

    fun getRecentMarketPricesInCache() {
        recentMarketPricesViewState.value = RecentMarketPricesViewState(true)
        addDisposable(marketPriceRepository.searchRecentMarketPriceInCache()
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
                        marketPriceData.date,
                        marketPriceData.nearbyMarketplaceNames
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
                    errorLiveData.postValue("An error has occurred")
                }
            ))
    }

    fun getSignedInUser(shouldGetFromServer: Boolean) {
        signedInUserViewState.value = SignedInUserViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser(shouldGetFromServer)
            .map {
                User(
                    it.userId,
                    it.username,
                    it.phoneNumber,
                    it.firstName,
                    it.lastName,
                    it.location,
                    it.gender
                )
            }
            .subscribe(
            {
                signedInUserViewState.postValue(SignedInUserViewState(isLoading = false, user = it))
            },
            {
                signedInUserViewState.postValue(SignedInUserViewState(isLoading = false))
                errorLiveData.postValue("An error has occurred")
            }
        ))
    }

    fun isFavoriteMarketPriceSearch(
        shouldGetUserFromServer: Boolean,
        country: String,
        market: String,
        category: String,
        product: String
    ) {
        isFavoriteMarketPriceSearchViewState.value = IsFavoriteMarketPriceSearchViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser(shouldGetUserFromServer)
            .flatMap {
                if (it.userId != null) {
                    return@flatMap marketPriceRepository.isFavorite(it.userId!!, country, market, category, product)
                }

                throw Throwable("User not signed in")
            }
            .subscribe(
                {
                    isFavoriteMarketPriceSearchViewState.postValue(
                        IsFavoriteMarketPriceSearchViewState(
                            isLoading = false,
                            isFavorite = it
                        ))
                },
                {
                    isFavoriteMarketPriceSearchViewState.postValue(
                        IsFavoriteMarketPriceSearchViewState(
                            isLoading = false,
                            isFavorite = false
                        ))
                    errorLiveData.postValue("An error has occurred")
                }
            ))
    }

    fun toggleFavorite(
        shouldGetUserFromServer: Boolean,
        country: String,
        market: String,
        category: String,
        product: String
    ) {
        isFavoriteMarketPriceSearchViewState.value = IsFavoriteMarketPriceSearchViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser(shouldGetUserFromServer)
            .flatMap {userData->
                if (userData.userId != null) {
                    return@flatMap marketPriceRepository.isFavorite(userData.userId!!, country, market, category, product)
                        .doOnSuccess {
                            if (it) {
                                marketPriceRepository.removeFromFavorite(userData.userId!!, country, market, category, product).blockingAwait()
                            } else {
                                marketPriceRepository.addToFavorite(userData.userId!!, country, market, category, product).blockingAwait()
                            }
                        }
                }

                throw Throwable("User not signed in")
            }
            .map {
                !it
            }
            .subscribe(
                {
                    isFavoriteMarketPriceSearchViewState.postValue(
                        IsFavoriteMarketPriceSearchViewState(
                            isLoading = false,
                            isFavorite = it
                        ))
                },
                {
                    isFavoriteMarketPriceSearchViewState.postValue(
                        IsFavoriteMarketPriceSearchViewState(
                            isLoading = false,
                            isFavorite = isFavoriteMarketPriceSearchViewState.value?.isFavorite ?: false
                        ))
                    errorLiveData.postValue("An error has occurred")
                }
            ))
    }

    fun syncFavoriteMarketPriceSearches() {
        addDisposable(userRepository.getSignedInUser(true)
            .flatMapCompletable {
                if (it.userId != null) {
                    return@flatMapCompletable marketPriceRepository.syncFavoriteMarketPriceSearches(it.userId!!)
                }

                Completable.complete()
            }
            .subscribe(
            {

            },
            {
                errorLiveData.postValue("An error has occurred")
            }
        ))
    }

    class Factory(
        private val marketPriceRepository: MarketPriceRepository,
        private val userRepository: UserRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MarketPriceViewModel(marketPriceRepository, userRepository) as T
        }
    }

}