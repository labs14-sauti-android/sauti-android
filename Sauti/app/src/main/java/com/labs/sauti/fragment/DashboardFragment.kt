package com.labs.sauti.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.labs.sauti.R
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private var onReplaceFragmentListener: OnReplaceFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        c_market_price.setOnClickListener {
            onReplaceFragmentListener?.onReplaceFragment(MarketPriceFragment::class.java)
        }

        c_exchange_rates.setOnClickListener {
            onReplaceFragmentListener?.onReplaceFragment(ExchangeRatesFragment::class.java)
        }

        c_card_info.setOnClickListener {
            onReplaceFragmentListener?.onReplaceFragment(TradeInfoFragment::class.java)
        }

        c_tax_calculator.setOnClickListener {
            onReplaceFragmentListener?.onReplaceFragment(TaxCalculatorFragment::class.java)
        }

        t_marketplace.setOnClickListener {
            onReplaceFragmentListener?.onReplaceFragment(MarketplaceFragment::class.java)
        }

        t_report.setOnClickListener {
            onReplaceFragmentListener?.onReplaceFragment(ReportFragment::class.java)
        }

        t_help.setOnClickListener {
            onReplaceFragmentListener?.onReplaceFragment(HelpFragment::class.java)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DashboardFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnReplaceFragmentListener) {
            onReplaceFragmentListener = context
        } else {
            throw RuntimeException("Context must implement OnReplaceFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onReplaceFragmentListener = null
    }

    interface OnReplaceFragmentListener {
        fun <T : Fragment> onReplaceFragment(c: Class<T>)
    }
}
