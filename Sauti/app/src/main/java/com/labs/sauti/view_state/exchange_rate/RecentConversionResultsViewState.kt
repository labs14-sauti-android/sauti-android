package com.labs.sauti.view_state.exchange_rate

import com.labs.sauti.model.ExchangeRateConversionResult

class RecentConversionResultsViewState(
    var isLoading: Boolean = true,
    var recentConversionResults: MutableList<ExchangeRateConversionResult>? = null
)