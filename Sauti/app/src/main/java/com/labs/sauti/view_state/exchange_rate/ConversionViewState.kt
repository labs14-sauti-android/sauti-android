package com.labs.sauti.view_state.exchange_rate

import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult

class ConversionViewState(
    var isLoading: Boolean = false,
    var conversionResult: ExchangeRateConversionResult? = null
)