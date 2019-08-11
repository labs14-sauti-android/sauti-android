package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.repository.ExchangeRateRepository
import com.labs.sauti.view_state.exchange_rate.ConversionViewState
import com.labs.sauti.view_state.exchange_rate.CurrenciesViewState
import com.labs.sauti.view_state.exchange_rate.RecentConversionResultsViewState

class ExchangeRateViewModel(private val exchangeRateRepository: ExchangeRateRepository): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<String>() }
    private val currenciesViewState by lazy { MutableLiveData<CurrenciesViewState>() }
    private val conversionViewState by lazy { MutableLiveData<ConversionViewState>() }
    private val recentConversionResultsViewState by lazy { MutableLiveData<RecentConversionResultsViewState>() }

    fun getErrorLiveData(): LiveData<String> = errorLiveData
    fun getCurrenciesViewState(): LiveData<CurrenciesViewState> = currenciesViewState
    fun getConversionViewState(): LiveData<ConversionViewState> = conversionViewState
    fun getRecentConversionResultsViewState(): LiveData<RecentConversionResultsViewState> = recentConversionResultsViewState

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
        addDisposable(exchangeRateRepository.convertCurrency(fromCurrency, toCurrency, amount)
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

    class Factory(private val exchangeRateRepository: ExchangeRateRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ExchangeRateViewModel(exchangeRateRepository) as T
        }
    }
}