package com.labs.sauti.fragment

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    enum class Type {
        DASHBOARD, MARKET_PRICE, TAX_CALCULATOR, TRADE_INFO, EXCHANGE_RATES, MARKETPLACE
    }

    abstract fun getFragmentType(): Type

}