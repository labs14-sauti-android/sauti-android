package com.labs.sauti.model.trade_info

import androidx.lifecycle.MutableLiveData
import java.text.DecimalFormat
import java.util.*

data class TradeInfoTaxes(
    var taxProduct: String,
    var currentCurrency: String,
    var endCurrency: String,
    var initialAmount: Double,
    var taxList : MutableList<Taxes>,
    var rate: Double,
    var totalAmount: Double = 0.0
) {


    fun getTaxesConversions() {
        val df = DecimalFormat("#,###.##")

        taxList.removeIf {
            it.taxPerc == "0"
        }

        taxList.forEach {
            val percent = it.taxPerc.toDouble()

            val preConvertTax = percent / 100
            val preConvertTaxAmt = initialAmount * preConvertTax
            val postConvertTaxAmt = preConvertTaxAmt * rate
            it.taxTitle += """($percent%): ${df.format(postConvertTaxAmt)} $endCurrency"""
            totalAmount += postConvertTaxAmt

        }
    }


}