package com.labs.sauti.model.trade_info

data class TradeInfoTaxes(
    var taxProduct: String,
    var currentCurrency: String,

    var initialAmount: Double,
    var taxList : List<Taxes>,
    var total: Double? = null
)