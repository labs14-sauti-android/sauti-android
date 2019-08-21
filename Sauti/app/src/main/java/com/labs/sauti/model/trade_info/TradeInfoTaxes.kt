package com.labs.sauti.model.trade_info

data class TradeInfoTaxes(
    var product: String,
    var currencyAmount: Double,
    var taxList : List<Taxes>,
    var total: Double? = null
)