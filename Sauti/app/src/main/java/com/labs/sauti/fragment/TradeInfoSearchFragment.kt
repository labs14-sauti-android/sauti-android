package com.labs.sauti.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.labs.sauti.R
import com.labs.sauti.views.SearchSpinnerCustomView
import kotlinx.android.synthetic.main.fragment_trade_info_search.*


class TradeInfoSearchFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_info_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sscv_trade_info.progressBarSVisibility()
        sscv_trade_info.addSearchHeader("What are you looking for?")
        sscv_trade_info.addSpinnerContents(listOf(" ",
            "Border Procedures",
            "Required Documents",
            "Border Agencies",
            "Tax Calculator",
            "Regulated Goods")
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = TradeInfoSearchFragment()
    }

    fun loadNextSpinner(next: SearchSpinnerCustomView) {
        //TODO:
    }
}

