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
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.TaxCalculationData
import com.labs.sauti.sp.SettingsSp
import com.labs.sauti.view_model.TaxCalculatorViewModel
import com.labs.sauti.view_model.TradeInfoViewModel
import com.labs.sauti.views.SearchSpinnerCustomView
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
        tradeInfoViewModel.setFirstSpinnerContent("Taxes")

        tradeInfoViewModel.getTradeInfoFirstSpinnerContent().observe(this, Observer {
            if(it != null) {
                loadFirstSpinner(sscv_tax_calculator_q_1, it,"What is your commodity category?" )
            }
        })

        //TODO: Extract String resources
        tradeInfoViewModel.getTradeInfoSecondSpinnerContent().observe(this, Observer{
            loadSecondSpinner(sscv_tax_calculator_q_2, it, "Select your product")

        })

        tradeInfoViewModel.getTradeInfoThirdSpinnerContent().observe(this, Observer {
            loadThirdSpinner(sscv_tax_calculator_q_3, it, "Select your product origin")
        })

        tradeInfoViewModel.getTradeInfoFourthSpinnerContent().observe(this, Observer {
            loadFourthSpinner(sscv_tax_calculator_q_4, it, "Select where you're going")
        })
    }

    fun loadFirstSpinner(next: SearchSpinnerCustomView, spinnerList : List<String>, headerString : String) {
        next.visibility = View.VISIBLE


        val countryNames = convertCountryNames(spinnerList)
        next.addSpinnerContents(countryNames)

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                category = parent.getItemAtPosition(position) as String

                if(!category.isNullOrEmpty()){
                    tradeInfoViewModel.setSecondSpinnerContent(category)

                }
            }

        }
        next.setSpinnerListener(listener)
        next.addSearchHeader(headerString)
    }

    fun loadSecondSpinner(second: SearchSpinnerCustomView, spinnerList : List<String>, headerString : String) {
        second.visibility = View.VISIBLE
            second.addSpinnerContents(spinnerList)

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                product = parent.getItemAtPosition(position) as String

                if(!product.isNullOrEmpty()){
                    tradeInfoViewModel.setThirdSpinnerContent(language, category, product)
                }
            }
        }

        second.setSpinnerListener(listener)
        second.addSearchHeader(headerString)
    }

    fun loadThirdSpinner(third: SearchSpinnerCustomView, spinnerList : List<String>, headerString : String) {
        third.visibility = View.VISIBLE


        third.addSpinnerContents(spinnerList)

        val thirdListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                origin = parent.getItemAtPosition(position) as String

                if(!origin.isNullOrEmpty()){
                    tradeInfoViewModel.setFourthSpinnerContent(language, category, product, origin)
                }

            }
        }

        third.setSpinnerListener(thirdListener)
        third.addSearchHeader(headerString)
    }

    private fun loadFourthSpinner(fourth: SearchSpinnerCustomView, spinnerList: List<String>, headerString: String) {
        fourth.visibility = View.VISIBLE
        val conversion = convertCountryNames(spinnerList)
        fourth.addSpinnerContents(conversion)

        val fourthListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                dest = parent.getItemAtPosition(position) as String

                if(dest.isNotEmpty()){
                    tradeInfoViewModel.setFifthSpinnerContent()
                }
            }
        }

        fourth.setSpinnerListener(fourthListener)
        fourth.addSearchHeader(headerString)
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
        val map = hashMapOf("BDI" to "Burundi",
            "DRC" to "Democratic Republic of the Congo",
            "KEN" to "Kenya",
            "MWI" to "Malawi",
            "RWA" to "Rwanda",
            "TZA" to "Tanzania",
            "UGA" to "Uganda")
        @JvmStatic
        fun newInstance() =
            TaxCalculatorSearchFragment()
    }

    fun convertCountryNames(countryList : List<String>) : List<String> {

        val countryNames = mutableListOf<String>()

        countryList.forEach {
            val string = map[it]
            if(string == null) {
                countryNames.add(it)
            } else {
                countryNames.add(string)
            }
        }

        return countryNames
    }

    interface OnTaxCalculatorSearchCompletedListener {
        fun onTaxCalculatorSearchCompleted(taxCalculationData: TaxCalculationData)
    }

}
