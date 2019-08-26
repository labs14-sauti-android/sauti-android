package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.authentication.User
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.repository.ExchangeRateRepository
import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_state.authentication.SignedInUserViewState
import com.labs.sauti.view_state.exchange_rate.ConversionViewState
import com.labs.sauti.view_state.exchange_rate.CurrenciesViewState
import com.labs.sauti.view_state.exchange_rate.IsFavoriteExchangeRateConversionViewState
import com.labs.sauti.view_state.exchange_rate.RecentConversionResultsViewState
import io.reactivex.Completable

class ExchangeRateViewModel(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<String>() }
    private val currenciesViewState by lazy { MutableLiveData<CurrenciesViewState>() }
    private val conversionViewState by lazy { MutableLiveData<ConversionViewState>() }
    private val recentConversionResultsViewState by lazy { MutableLiveData<RecentConversionResultsViewState>() }

    private val signedInUserViewState by lazy { MutableLiveData<SignedInUserViewState>() }
    private val isFavoriteExchangeRateConversionViewState by lazy { MutableLiveData<IsFavoriteExchangeRateConversionViewState>() }

    fun getErrorLiveData(): LiveData<String> = errorLiveData
    fun getCurrenciesViewState(): LiveData<CurrenciesViewState> = currenciesViewState
    fun getConversionViewState(): LiveData<ConversionViewState> = conversionViewState
    fun getRecentConversionResultsViewState(): LiveData<RecentConversionResultsViewState> = recentConversionResultsViewState

    fun getSignedInUserViewState(): LiveData<SignedInUserViewState> = signedInUserViewState
    fun getIsFavoriteExchangeRateConversionViewState(): LiveData<IsFavoriteExchangeRateConversionViewState> = isFavoriteExchangeRateConversionViewState

    fun getCurrencies() {
        currenciesViewState.value = CurrenciesViewState(true)
        addDisposable(exchangeRateRepository.getExchangeRates()
            .map {
                it.mapNotNull { exchangeRate ->
                    exchangeRate.currency
                }.toMutableList()
            }
            .subscribe(
            {
                currenciesViewState.postValue(CurrenciesViewState(false, it))
            },
            {
                errorLiveData.postValue(it.message)
            }
        ))
    }

    fun convert(fromCurrency: String, toCurrency: String, amount: Double) {
        conversionViewState.value = ConversionViewState(true)
        addDisposable(exchangeRateRepository.convertCurrency(true, fromCurrency, toCurrency, amount)
            .map {
                ExchangeRateConversionResult(
                    it.fromCurrency,
                    it.toCurrency,
                    it.toPerFrom,
                    it.amount,
                    it.result
                )
            }
            .subscribe(
            {
                conversionViewState.postValue(
                    ConversionViewState(
                        false,
                        it
                    )
                )
            },
            {
                errorLiveData.postValue(it.message)
            }
        ))
    }

    fun getRecentConversionResults() { // TODO combine with inCache with flag
        recentConversionResultsViewState.value = RecentConversionResultsViewState(true)
        addDisposable(exchangeRateRepository.getRecentConversionResults()
            .map {
                it.map { conversionResult ->
                    ExchangeRateConversionResult(
                        conversionResult.fromCurrency,
                        conversionResult.toCurrency,
                        conversionResult.toPerFrom,
                        conversionResult.amount,
                        conversionResult.result
                    )
                }.toMutableList()
            }
            .subscribe(
            {
                recentConversionResultsViewState.postValue(
                    RecentConversionResultsViewState(
                        false,
                        it
                    )
                )
            },
            {
                errorLiveData.postValue(it.message)
            }
        ))
    }

    fun getRecentConversionResultsInCache() {
        recentConversionResultsViewState.value = RecentConversionResultsViewState(true)
        addDisposable(exchangeRateRepository.getRecentConversionResultsInCache()
            .map {
                it.map { conversionResult ->
                    ExchangeRateConversionResult(
                        conversionResult.fromCurrency,
                        conversionResult.toCurrency,
                        conversionResult.toPerFrom,
                        conversionResult.amount,
                        conversionResult.result
                    )
                }.toMutableList()
            }
            .subscribe(
                {
                    recentConversionResultsViewState.postValue(
                        RecentConversionResultsViewState(
                            false,
                            it
                        )
                    )
                },
                {
                    errorLiveData.postValue(it.message)
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

    fun syncFavoriteConversions() {
        addDisposable(userRepository.getSignedInUser(true)
            .flatMapCompletable {
                if (it.userId != null) {
                    return@flatMapCompletable exchangeRateRepository.syncFavoriteExchangeRateConversions(it.userId!!)
                }

                Completable.complete()
            }
            .subscribe(
                {

                },
                {
                    errorLiveData.postValue("An error has occurred")
                }
            )
        )
    }

    fun isFavoriteConversion(
        shouldGetUserFromServer: Boolean,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ) {
        isFavoriteExchangeRateConversionViewState.value = IsFavoriteExchangeRateConversionViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser(shouldGetUserFromServer)
            .flatMap {
                if (it.userId != null) {
                    return@flatMap exchangeRateRepository.isFavorite(it.userId!!, fromCurrency, toCurrency, amount)
                }

                throw Throwable("User not signed in")
            }
            .subscribe(
                {
                    isFavoriteExchangeRateConversionViewState.postValue(
                        IsFavoriteExchangeRateConversionViewState(
                            isLoading = false,
                            isFavorite = it
                        )
                    )
                },
                {
                    isFavoriteExchangeRateConversionViewState.postValue(
                        IsFavoriteExchangeRateConversionViewState(
                            isLoading = false,
                            isFavorite = false
                        )
                    )
                    errorLiveData.postValue("An error has occurred")
                }
            )
        )
    }

    fun toggleFavorite(
        shouldGetUserFromServer: Boolean,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ) {
        isFavoriteExchangeRateConversionViewState.value = IsFavoriteExchangeRateConversionViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser(shouldGetUserFromServer)
            .flatMap {userData->
                if (userData.userId != null) {
                    return@flatMap exchangeRateRepository.isFavorite(userData.userId!!, fromCurrency, toCurrency, amount)
                        .doOnSuccess {
                            if (it) {
                                exchangeRateRepository.removeFromFavorite(userData.userId!!, fromCurrency, toCurrency, amount).blockingAwait()
                            } else {
                                exchangeRateRepository.addToFavorite(userData.userId!!, fromCurrency, toCurrency, amount).blockingAwait()
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
                    isFavoriteExchangeRateConversionViewState.postValue(
                        IsFavoriteExchangeRateConversionViewState(
                            isLoading = false,
                            isFavorite = it
                        )
                    )
                },
                {
                    isFavoriteExchangeRateConversionViewState.postValue(
                        IsFavoriteExchangeRateConversionViewState(
                            isLoading = false,
                            isFavorite = false
                        )
                    )
                    errorLiveData.postValue("An error has occurred")
                }
            )
        )
    }

    class Factory(
        private val exchangeRateRepository: ExchangeRateRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ExchangeRateViewModel(exchangeRateRepository, userRepository) as T
        }
    }
}