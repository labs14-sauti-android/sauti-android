package com.labs.sauti.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.labs.sauti.R
import kotlinx.android.synthetic.main.fragment_trade_info.*

class TradeInfoFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO Hide the constraint layout unless clicked. Handled via clicklistener. 
        cl_expanded_tradeinfo.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TradeInfoFragment()
    }

}
