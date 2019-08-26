package com.labs.sauti.model.trade_info

import androidx.lifecycle.MutableLiveData
import java.util.*

data class TradeInfoTaxes(
    var taxProduct: String,
    var currentCurrency: String,
    var endCurrency: String,
    var initialAmount: Double,
    var taxList : List<Taxes>,
    var rate: Double,
    var total: Double? = null
) {


    fun getTaxesConversions() {

    }

    fun getTotal(){

    }

}