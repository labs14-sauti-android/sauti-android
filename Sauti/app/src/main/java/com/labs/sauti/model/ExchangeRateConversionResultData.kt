package com.labs.sauti.model

class ExchangeRateConversionResultData(
    var fromCurrency: String,
    var toCurrency: String,
    var toPerFrom: Double, // eg. KES = from, UGX = to. 1KES = toPerFrom UGX.
    var amount: Double,
    var result: Double
)