package com.labs.sauti.model.exchange_rate

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExchangeRateConversionResult(
    var fromCurrency: String,
    var toCurrency: String,
    var toPerFrom: Double, // eg. KES = from, UGX = to. 1KES = toPerFrom UGX.
    var amount: Double,
    var result: Double
): Parcelable