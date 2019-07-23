package com.labs.sauti.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.labs.sauti.R

class TradeInfoFragment : BaseFragment() {
    override fun getFragmentType(): Type = Type.TRADE_INFO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_info, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TradeInfoFragment()
    }

}
