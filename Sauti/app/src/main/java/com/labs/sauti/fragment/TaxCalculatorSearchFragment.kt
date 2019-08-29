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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import com.labs.sauti.sp.SettingsSp
import com.labs.sauti.view_model.TradeInfoViewModel
import com.labs.sauti.views.SearchSpinnerCustomView
import kotlinx.android.synthetic.main.fragment_tax_calculator_search.*
import javax.inject.Inject


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
    lateinit var destChoice: String
    lateinit var origin : String
    lateinit var currencyUser: String
    lateinit var currencyTo: String
    var currencyUserToRate : Double = 1.0
    lateinit var currencyConversions : MutableList<ExchangeRateData>




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

        setTaxTradeInfoQuestions()

        tradeInfoViewModel.getTradeInfoFirstSpinnerContent().observe(this, Observer {
            if(it != null) {
                loadFirstSpinner(sscv_tax_calculator_q_1, it)
            }
        })

        //TODO: Extract String resources
        tradeInfoViewModel.getTradeInfoSecondSpinnerContent().observe(this, Observer{
            loadSecondSpinner(sscv_tax_calculator_q_2, it)
        })

        tradeInfoViewModel.getTradeInfoThirdSpinnerContent().observe(this, Observer {
            loadThirdSpinner(sscv_tax_calculator_q_3, it)
        })

        tradeInfoViewModel.getTradeInfoFourthSpinnerContent().observe(this, Observer {
            loadFourthSpinner(sscv_tax_calculator_q_4, it)
        })

        tradeInfoViewModel.getTaxCalcCurrentSpinnerContent().observe(this, Observer {
            if(it != null) {
                currencyConversions = it
                val currencyStrings = mutableListOf<String>()
                it.forEach{currencyString ->
                    currencyStrings.add((currencyString.currency as String))
                }
                loadFifthSpinner(sscv_tax_calculator_q_5, currencyStrings)
            }
        })

        tradeInfoViewModel.getTaxCalcConversionTextConent().observe(this, Observer {

            sscv_tax_calculator_q_6.visibility = View.VISIBLE
            t_tax_calculator_value.text = "What is the approximate value of your goods in " + currencyTo + "?"

        })

        tradeInfoViewModel.getSearchTaxCalculations().observe(this, Observer {
            if(it != null) {
                onTaxCalculatorSearchCompletedListener?.onTaxCalculatorSearchCompleted(it)
                onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)

                fragmentManager!!.popBackStack()
            }
        })





        b_tax_calculator_search.setOnClickListener {

            if(sscv_tax_calculator_q_6.visibility == View.VISIBLE) {
                val amountS = et_tax_calculator.text.toString()
                val amount = if(amountS.isEmpty()) 0.0 else amountS.toDouble()

                if(amount <= 0 || sscv_tax_calculator_q_6.visibility == View.INVISIBLE) {
                    Toast.makeText(context!!, "Please input an amount greater than 0", Toast.LENGTH_LONG).show()
                } else {
                    var currencyFromRate: Double = 1.0
                    var currencyToRate: Double = 1.0

                    currencyConversions.forEach{from ->
                        if(from.currency == currencyTo) {
                            currencyFromRate = from.rate as Double
                        }
                    }

                    currencyConversions.forEach{to ->
                        if(to.currency == currencyUser) {
                            currencyToRate = to.rate as Double
                        }
                    }

                    currencyUserToRate = currencyFromRate / currencyToRate

                    val usdAmount = amount * currencyToRate

                    if(usdAmount < 2000) {
                        tradeInfoViewModel.searchTaxCalculations(language, category, product, origin, dest, amount, currencyUser, currencyTo, currencyUserToRate, 1.0)

                    } else {
                        tradeInfoViewModel.searchTaxCalculations(language, category, product, origin, dest, amount, currencyUser, currencyTo, currencyUserToRate, 2001.0)
                    }

                }
            }

        }
    }

    //TODO: Translate the hardcoded text
    fun setTaxTradeInfoQuestions() {
        sscv_tax_calculator_q_1.addSearchHeader("What category of commodities are you trading?")
        sscv_tax_calculator_q_2.addSearchHeader("What commodity are you trading?")
        sscv_tax_calculator_q_3.addSearchHeader("Where was your product made/originate?")
        sscv_tax_calculator_q_4.addSearchHeader("Where are you selling your goods?")
        sscv_tax_calculator_q_5.addSearchHeader("What is your currency?")
    }

    fun loadFirstSpinner(next: SearchSpinnerCustomView, spinnerList : List<String>) {

        next.addSpinnerContents(spinnerList)

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                category = parent.getItemAtPosition(position) as String

                if(!category.isNullOrEmpty()){
                    tradeInfoViewModel.setSecondSpinnerContent(category)

                } else {
                    sscv_tax_calculator_q_6.visibility = View.INVISIBLE
                }
            }

        }
        next.setSpinnerListener(listener)

    }

    fun loadSecondSpinner(second: SearchSpinnerCustomView, spinnerList : List<String>) {
        second.addSpinnerContents(spinnerList)

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                product = parent.getItemAtPosition(position) as String

                if(!product.isNullOrEmpty()){
                    tradeInfoViewModel.setThirdSpinnerContent(language, category, product)
                } else {
                    sscv_tax_calculator_q_6.visibility = View.INVISIBLE
                }
            }
        }

        second.setSpinnerListener(listener)
    }

    fun loadThirdSpinner(third: SearchSpinnerCustomView, spinnerList : List<String>) {
        third.addSpinnerContents(spinnerList)

        val thirdListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                origin = parent.getItemAtPosition(position) as String

                if(!origin.isNullOrEmpty()){
                    tradeInfoViewModel.setFourthSpinnerContent(language, category, product, origin)
                } else {
                    sscv_tax_calculator_q_6.visibility = View.INVISIBLE
                }

            }
        }

        third.setSpinnerListener(thirdListener)
    }

    private fun loadFourthSpinner(fourth: SearchSpinnerCustomView, spinnerList: List<String>) {
        val conversion = convertCountryNames(spinnerList)
        fourth.addSpinnerContents(conversion)

        val fourthListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                destChoice = parent.getItemAtPosition(position) as String

                if(destChoice.isNotEmpty()){
                    //TODO: Set Currency Spinner
                    dest = convertCountrytoCountryCode(destChoice)
                    tradeInfoViewModel.setTaxCalcCurrencySpinnerContent(language, category, product, origin, dest)
                    when(dest) {
                        "KEN"-> {currencyTo = "KES" }
                        "UGA"-> {currencyTo = "UGX"}
                    }
                } else {
                    sscv_tax_calculator_q_6.visibility = View.INVISIBLE
                }
            }
        }

        fourth.setSpinnerListener(fourthListener)
    }

    private fun loadFifthSpinner(fifth: SearchSpinnerCustomView, spinnerList: List<String>) {
        fifth.addSpinnerContents(spinnerList)

        val fifthListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                if(position != 0) {
                    currencyUser = parent.getItemAtPosition(position) as String
                    tradeInfoViewModel.setTaxCalcConversionTextConent(currencyUser)
                } else {
                    sscv_tax_calculator_q_6.visibility = View.INVISIBLE
                }
            }
        }

        fifth.setSpinnerListener(fifthListener)
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

    fun convertCountrytoCountryCode(countryName : String) : String {

        when(countryName) {
            "Kenya" -> (return "KEN")
            "Burundi"-> (return "BDI")
            "Democratic Republic of the Congo"-> (return "DRC")
            "Malawi"-> (return "MWI")
            "Rwanda"-> (return "RWA")
            "Tanzania"-> (return "TZA")
            "Uganda"-> (return "UGA")
            else -> return ""
        }
    }

    interface OnTaxCalculatorSearchCompletedListener {
        fun onTaxCalculatorSearchCompleted(tradeInfoTaxes: TradeInfoTaxes)
    }

}
