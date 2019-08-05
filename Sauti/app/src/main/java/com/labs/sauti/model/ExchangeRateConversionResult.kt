package com.labs.sauti.model

class ExchangeRateConversionResult(
    var fromCurrency: String,
    var toCurrency: String,
    var toPerFrom: Double,
    var amount: Double,
    var result: Double
)