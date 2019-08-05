package com.labs.sauti.view_state.exchange_rate

import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult

class RecentConversionResultsViewState(
    var isLoading: Boolean = true,
    var recentConversionResults: MutableList<ExchangeRateConversionResult>? = null
)