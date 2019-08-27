package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.mapper.ExchangeRateConversionResultDataToExchangeRateConversionResultMapper
import com.labs.sauti.mapper.MarketPriceDataToMarketPriceMapper
import com.labs.sauti.repository.ExchangeRateRepository
import com.labs.sauti.repository.MarketPriceRepository
import com.labs.sauti.repository.TradeInfoRepository
import com.labs.sauti.view_state.dashboard.RecentSearchesViewState
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class DashboardRecentSearchesViewModel(
    private val marketPriceRepository: MarketPriceRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val tradeInfoRepository: TradeInfoRepository
): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<String>() }
    private val recentSearchesViewState by lazy { MutableLiveData<RecentSearchesViewState>() }

    fun getErrorLiveData(): LiveData<String> = errorLiveData
    fun getRecentSearchesViewState(): LiveData<RecentSearchesViewState> = recentSearchesViewState

    fun getRecentSearches() {
        recentSearchesViewState.value = RecentSearchesViewState(isLoading = true)
        addDisposable(
            Single.fromCallable {
                val marketPrices = marketPriceRepository.searchRecentMarketPrices().flatMap {
                    MarketPriceDataToMarketPriceMapper().single(it)
                }
                    .onErrorResumeNext(Single.just(mutableListOf()))
                    .blockingGet()

                val exchangeRateConversionResults =
                    exchangeRateRepository.getRecentConversionResults().flatMap {
                        ExchangeRateConversionResultDataToExchangeRateConversionResultMapper().single(it)
                    }
                        .onErrorResumeNext(Single.just(mutableListOf()))
                        .blockingGet()

                val recentSearches = mutableListOf<Any>()

                recentSearches.addAll(marketPrices)
                recentSearches.addAll(exchangeRateConversionResults)
                // TODO Patrick add trade info here

                recentSearches
            }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        recentSearchesViewState.postValue(RecentSearchesViewState(
                            isLoading = false,
                            recentSearches = it
                        ))
                    },
                    {
                        recentSearchesViewState.postValue(RecentSearchesViewState(isLoading = false))
                        errorLiveData.postValue("An error has occurred")
                    }
                ))
    }

    class Factory(
        private val marketPriceRepository: MarketPriceRepository,
        private val exchangeRateRepository: ExchangeRateRepository,
        private val tradeInfoRepository: TradeInfoRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DashboardRecentSearchesViewModel(
                marketPriceRepository,
                exchangeRateRepository,
                tradeInfoRepository
            ) as T
        }
    }
}