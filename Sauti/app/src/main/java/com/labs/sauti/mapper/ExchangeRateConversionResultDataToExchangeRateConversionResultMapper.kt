package com.labs.sauti.mapper

import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResultData

class ExchangeRateConversionResultDataToExchangeRateConversionResultMapper:
        Mapper<ExchangeRateConversionResultData, ExchangeRateConversionResult>() {
    override fun mapFrom(from: ExchangeRateConversionResultData): ExchangeRateConversionResult {
        return ExchangeRateConversionResult(
            from.fromCurrency,
            from.toCurrency,
            from.toPerFrom,
            from.amount,
            from.result
        )
    }
}