package com.labs.sauti.fragment


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.TaxCalculationData
import com.labs.sauti.sp.SettingsSp
import com.labs.sauti.view_model.TaxCalculatorViewModel
import com.labs.sauti.view_model.TradeInfoViewModel
import kotlinx.android.synthetic.main.fragment_tax_calculator_search.*
import javax.inject.Inject

// TODO fullscreen
class TaxCalculatorSearchFragment : Fragment() {

    private var onTaxCalculatorSearchCompletedListener: OnTaxCalculatorSearchCompletedListener? = null
    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory
    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var language : String
    lateinit var category: String
    lateinit var product : String
    lateinit var dest : String
    lateinit var origin : String
    var value:  Double? = 0.0




    private val networkChangedReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetworkHelper.hasNetworkConnection(context!!)) {
                t_tax_calculator_warning.visibility = View.GONE
            } else {
                t_tax_calculator_warning.visibility = View.VISIBLE
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            firebaseAnalytics = FirebaseAnalytics.getInstance(it)
            (it.applicationContext as SautiApp).getTaxCalculatorComponent().inject(this)

            tradeInfoViewModel = ViewModelProviders.of(this, tradeInfoViewModelFactory)
                .get(TradeInfoViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tax_calculator_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context!!.registerReceiver(networkChangedReceiver, IntentFilter().also {
            it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        })

        language = SettingsSp(context!!).getSelectedLanguage().toUpperCase()
        tradeInfoViewModel.setLanguage(language)
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (parentFragment is OnTaxCalculatorSearchCompletedListener) {
            onTaxCalculatorSearchCompletedListener = parentFragment as OnTaxCalculatorSearchCompletedListener
        } else {
            throw RuntimeException("parentFragment must implement OnTaxCalculatorSearchCompletedListener")
        }

        if (parentFragment is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = parentFragment as OnFragmentFullScreenStateChangedListener
            onFragmentFullScreenStateChangedListener!!.onFragmetFullScreenStateChanged(true)
        } else {
            throw RuntimeException("parentFragment must implement OnFragmentFullScreenStateChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onTaxCalculatorSearchCompletedListener = null
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
        onFragmentFullScreenStateChangedListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TaxCalculatorSearchFragment()
    }

    interface OnTaxCalculatorSearchCompletedListener {
        fun onTaxCalculatorSearchCompleted(taxCalculationData: TaxCalculationData)
    }

}
