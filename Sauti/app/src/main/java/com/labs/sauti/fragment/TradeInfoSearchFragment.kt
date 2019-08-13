package com.labs.sauti.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.sp.SettingsSp
import com.labs.sauti.view_model.TradeInfoViewModel
import com.labs.sauti.views.SearchSpinnerCustomView
import kotlinx.android.synthetic.main.fragment_trade_info_search.*
import javax.inject.Inject


class TradeInfoSearchFragment : Fragment() {

    private var onTradeSearchCompletedListener : OnTradeInfoSearchCompletedListener? = null
    private var onFragmentFullScreenStateChangedListener : OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory
    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var categoryListener : View.OnClickListener

    private lateinit var buttonList : List<Button>
    private lateinit var searchSpinnerList : List<SearchSpinnerCustomView>

    private lateinit var firstSpinnerSelected : String
    private lateinit var language : String


    private val networkChangedReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetworkHelper.hasNetworkConnection(context!!)) {
                t_trade_info_warning.visibility = View.GONE
            } else {
                t_trade_info_warning.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            firebaseAnalytics = FirebaseAnalytics.getInstance(it)
            (it.applicationContext as SautiApp).getTradeInfoComponent().inject(this)

            tradeInfoViewModel = ViewModelProviders.of(this, tradeInfoViewModelFactory)
                .get(TradeInfoViewModel::class.java)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trade_info_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context!!.registerReceiver(networkChangedReceiver, IntentFilter().also {
            it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        })

        //TODO: Change the text in each of the custom spinners
        //setTranslatedTexts()

        //Places all buttons in a list, sets clicklisteners and disable search button.
        buttonSpinnerSetup()

        language = SettingsSp(context!!).getSelectedLanguage()
        tradeInfoViewModel.setLanguage(language)

        tradeInfoViewModel.setFirstSpinnerContent()

        //TODO: Extract String resources
        tradeInfoViewModel.getTradeInfoFirstSpinnerContent().observe(this, Observer {
            val category = tradeInfoViewModel.getTradeInfoCategory().value as String
            if(category == "Regulated Goods") {
                loadFirstSpinner(sscv_trade_info_q_1, it, "Regulated Goods")
            } else {
                loadFirstSpinner(sscv_trade_info_q_1, it, "What is your commodity category?")
            }
        })


        //TODO: Extract String resources
        tradeInfoViewModel.getTradeInfoSecondSpinnerContent().observe(this, Observer{
            val category = tradeInfoViewModel.getTradeInfoCategory().value as String
            if(category == "Regulated Goods"){
                loadSecondSpinner(sscv_trade_info_q_2, it, "Regulation Type")
            } else {
                loadSecondSpinner(sscv_trade_info_q_2, it, "Select your product")
            }
        })

        tradeInfoViewModel.getSearchRegulatedGoodsLiveData().observe(this, Observer {

            if(it != null){
                onTradeSearchCompletedListener?.OnTradeInfoSearchCompleted(it)
                b_trade_info_search.isEnabled = true
            } else {
                b_trade_info_search.isEnabled = false
            }
        })

        b_trade_info_search.setOnClickListener {

            if(!(sscv_trade_info_q_2.getSpinnerSelected().isNullOrEmpty())){
                onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
                fragmentManager!!.popBackStack()

//                onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
//                fragmentManager!!.popBackStack()


//                val regulatedType = sscv_trade_info_q_1.getSpinnerSelected()
//                if(!regulatedType.isNullOrEmpty()) {
//                    val country = sscv_trade_info_q_1.getSpinnerSelected()
//                    lateinit var countryCode: String
//                    map.forEach mapBreak@{
//                        if (it.value == country) {
//                            countryCode = it.key
//                            return@mapBreak
//                        }
//                    }
//                       tradeInfoViewModel.searchRegulatedGoods(language.toUpperCase(), countryCode, regulatedType )
//                }
            }
        }

    }


    private fun buttonSpinnerSetup() {

        categoryListener = View.OnClickListener { v ->
            val b = v as Button

            b.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))

            val category = b.text.toString()
            tradeInfoViewModel.setFirstSpinnerContent(category)

            buttonList.forEach {button->
                if(button.id != b.id) {
                    button.background.setTint(ContextCompat.getColor(context!!, R.color.colorButtonResponse))
                }
            }

            for (i in 1..3) {
                searchSpinnerList[i].visibility = View.GONE
            }

            b_trade_info_search.isEnabled = false
        }

        buttonList = listOf(b_trade_info_procedures,
            b_trade_info_documents,
            b_trade_info_agencies,
            b_trade_info_regulated)
        searchSpinnerList = listOf(sscv_trade_info_q_1,
            sscv_trade_info_q_2,
            sscv_trade_info_q_3,
            sscv_trade_info_q_4,
            sscv_trade_info_q_5)

        b_trade_info_procedures.setOnClickListener(categoryListener)
        b_trade_info_documents.setOnClickListener(categoryListener)
        b_trade_info_agencies.setOnClickListener(categoryListener)
        b_trade_info_regulated.setOnClickListener(categoryListener)


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(parentFragment is OnTradeInfoSearchCompletedListener) {
            onTradeSearchCompletedListener = parentFragment as OnTradeInfoSearchCompletedListener
        }  else {
            throw RuntimeException("parentFragment must implement OnTradeInfoSearchCompletedListener")
        }

        if(parentFragment is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = parentFragment as OnFragmentFullScreenStateChangedListener
            onFragmentFullScreenStateChangedListener!!.onFragmetFullScreenStateChanged(true)
        }  else {
            throw RuntimeException("parentFragment must implement OnFragmentFullScreenStateChangedListener")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(networkChangedReceiver)
    }

    override fun onDetach() {
        super.onDetach()
        onTradeSearchCompletedListener = null

        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
        onFragmentFullScreenStateChangedListener = null

    }

    private fun setTranslatedTexts() {
        //TODO
    }

    interface OnTradeInfoSearchCompletedListener {
        fun OnTradeInfoSearchCompleted(tradeInfo: TradeInfo)
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
        fun newInstance() = TradeInfoSearchFragment()
    }

    fun loadFirstSpinner(next: SearchSpinnerCustomView, spinnerList : List<String>, headerString : String) {
        next.visibility = View.VISIBLE

        if(headerString == "Regulated Goods") {
            val countryNames = convertCountryNames(spinnerList)
            next.addSpinnerContents(countryNames)

            val listener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val country = next.getSpinnerSelected()

                    if(!country.isNullOrEmpty()){
                        map.forEach mapBreak@{
                            if(it.value == country) {
                                firstSpinnerSelected = it.key
                                return@mapBreak
                            }
                        }

                        //This will do the final search
                        //Language and Country are all we need
                        tradeInfoViewModel.setSecondSpinnerContent(firstSpinnerSelected)

                    }
                }

            }

            next.setSpinnerListener(listener)

        } else {
            next.addSpinnerContents(spinnerList)

            val listener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val product = next.getSpinnerSelected()

                    if(!product.isNullOrEmpty()){
                        Toast.makeText(context, product, Toast.LENGTH_LONG).show()

                    }
                }
            }

            next.setSpinnerListener(listener)
        }

        next.addSearchHeader(headerString)
    }

    fun loadSecondSpinner(second: SearchSpinnerCustomView, spinnerList : List<String>, headerString : String) {
        second.visibility = View.VISIBLE

        if(headerString == "Regulation Type") {
            second.addSpinnerContents(spinnerList)
            second.addSearchHeader(headerString)

            val secondListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    val regulatedType = parent!!.selectedItem as String
                    val regulatedType = second.getSpinnerSelected()

                    if(!regulatedType.isNullOrEmpty()){
                        val country = sscv_trade_info_q_1.getSpinnerSelected()
                        lateinit var countryCode : String
                        map.forEach mapBreak@{
                            if(it.value == country) {
                                countryCode = it.key
                                return@mapBreak
                            }
                        }
                        tradeInfoViewModel.searchRegulatedGoods(language.toUpperCase(), countryCode, regulatedType)

                    }
                }
            }

            second.setSpinnerListener(secondListener)

        } else {
            second.addSpinnerContents(spinnerList)

            val listener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val product = second.getSpinnerSelected()

                    if(!product.isNullOrEmpty()){

                    }
                }
            }

            second.setSpinnerListener(listener)
        }

        second.addSearchHeader(headerString)
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
}

