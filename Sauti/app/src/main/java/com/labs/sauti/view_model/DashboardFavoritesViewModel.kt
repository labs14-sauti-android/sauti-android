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
import com.labs.sauti.view_state.dashboard.FavoritesViewState

class DashboardFavoritesViewModel(
    private val userRepository: UserRepository,
    private val marketPriceRepository: MarketPriceRepository
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
                    val favorites = mutableListOf<Any>()
                    val favoriteMarketPriceDataList =
                        marketPriceRepository.getFavoriteMarketPrices(it.userId!!).blockingGet()

                    val favoriteMarketPrices = favoriteMarketPriceDataList.map {marketPriceData ->
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

                    // TODO exchange rate, and trade info favorites
                    // TODO sort by timeFavorited

                    favorites.addAll(favoriteMarketPrices as MutableList<Any>)

                    return@map favorites
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
        private val marketPriceRepository: MarketPriceRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DashboardFavoritesViewModel(userRepository, marketPriceRepository) as T
        }
    }
}