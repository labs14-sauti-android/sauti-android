package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.authentication.User
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.repository.ExchangeRateRepository
import com.labs.sauti.repository.MarketPriceRepository
import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_state.authentication.SignedInUserViewState
import com.labs.sauti.view_state.dashboard.FavoritesViewState

class DashboardFavoritesViewModel(
    private val userRepository: UserRepository,
    private val marketPriceRepository: MarketPriceRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) : BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<String>() }
    private val signedInUserViewState by lazy { MutableLiveData<SignedInUserViewState>() }
    private val favoritesViewState by lazy { MutableLiveData<FavoritesViewState>() }

    fun getErrorLiveData(): LiveData<String> = errorLiveData
    fun getSignedInUserViewState(): LiveData<SignedInUserViewState> = signedInUserViewState
    fun getFavoritesViewState(): LiveData<FavoritesViewState> = favoritesViewState

    fun getSignedInUser(hasNetworkConnection: Boolean) {
        signedInUserViewState.value = SignedInUserViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser(hasNetworkConnection)
            .map {
                User(
                    userId = it.userId,
                    username = it.username,
                    phoneNumber = it.phoneNumber,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    location = it.location,
                    gender = it.gender
                )
            }
            .subscribe(
                {
                    signedInUserViewState.postValue(
                        SignedInUserViewState(
                            isLoading = false,
                            user = it
                        )
                    )
                },
                {
                    signedInUserViewState.postValue(
                        SignedInUserViewState(
                            isLoading = false
                        )
                    )
                    errorLiveData.postValue("An error has occurred")
                }
            ))
    }

    fun getFavorites(hasNetworkConnection: Boolean) {
        favoritesViewState.value = FavoritesViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser(hasNetworkConnection)
            .map {
                if (it.userId != null) {
                    // TODO exchange rate, and trade info favorites
                    // TODO sort by timeFavorited

                    val favoriteMarketPriceDataTimestampMap =
                        marketPriceRepository.getFavoriteMarketPrices(it.userId!!).blockingGet()
                            .mapKeys {pair ->
                                MarketPrice(
                                    pair.key.country,
                                    pair.key.market,
                                    pair.key.productAgg,
                                    pair.key.productCat,
                                    pair.key.product,
                                    pair.key.wholesale,
                                    pair.key.retail,
                                    pair.key.currency,
                                    pair.key.date,
                                    pair.key.nearbyMarketplaceNames
                                )
                            }

                    val favoriteExchangeRateConversionResultDataTimestampMap =
                        exchangeRateRepository.getFavoriteExchangeRateConversionResults(it.userId!!).blockingGet()
                            .mapKeys {pair ->
                                ExchangeRateConversionResult(
                                    pair.key.fromCurrency,
                                    pair.key.toCurrency,
                                    pair.key.toPerFrom,
                                    pair.key.amount,
                                    pair.key.result
                                )
                            }

                    val favoriteTimestampMap = hashMapOf<Any, Long>()
                    favoriteTimestampMap.putAll(favoriteMarketPriceDataTimestampMap)
                    favoriteTimestampMap.putAll(favoriteExchangeRateConversionResultDataTimestampMap)
                    // TODO Patrick, use .putAll(favoriteTradeInfoTimestampMap)

                    // insert sort, more recent on lower index
                    val favoriteTimestampList = mutableListOf<Pair<Any, Long>>()
                    favoriteTimestampMap.forEach {favoriteTimestampPairFromMap ->
                        var isInserted = false
                        for ((i, favoriteTimestampPairFromList) in favoriteTimestampList.withIndex()) {
                            if (favoriteTimestampPairFromMap.value > favoriteTimestampPairFromList.second) {
                                favoriteTimestampList.add(i, Pair(favoriteTimestampPairFromMap.key, favoriteTimestampPairFromMap.value))
                                isInserted = true
                                break
                            }
                        }
                        if (!isInserted) {
                            favoriteTimestampList.add(Pair(favoriteTimestampPairFromMap.key, favoriteTimestampPairFromMap.value))
                        }
                    }

                    return@map favoriteTimestampList.map {pair ->
                        pair.first
                    }.toMutableList()
                }

                mutableListOf<Any>()
            }
            .subscribe(
                {
                    favoritesViewState.postValue(FavoritesViewState(isLoading = false, favorites = it))
                },
                {
                    favoritesViewState.postValue(FavoritesViewState(isLoading = false))
                    errorLiveData.postValue("An error has occurred")
                }
            )
        )
    }

    class Factory(
        private val userRepository: UserRepository,
        private val marketPriceRepository: MarketPriceRepository,
        private val exchangeRateRepository: ExchangeRateRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DashboardFavoritesViewModel(userRepository, marketPriceRepository, exchangeRateRepository) as T
        }
    }
}