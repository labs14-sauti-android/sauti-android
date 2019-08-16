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
import androidx.core.content.ContextCompat
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
    private var tradeInfoCategory : String = "Border Procedures"

    lateinit var product : String
    lateinit var category: String
    lateinit var origin : String
    lateinit var dest : String
    var value:  Double? = 0.0

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

        language = SettingsSp(context!!).getSelectedLanguage().toUpperCase()
        tradeInfoViewModel.setLanguage(language)

//        tradeInfoViewModel.setFirstSpinnerContent()

        //TODO: Extract String resources
        tradeInfoViewModel.getTradeInfoFirstSpinnerContent().observe(this, Observer {
            if(tradeInfoCategory == "Regulated Goods") {
                loadFirstSpinner(sscv_trade_info_q_1, it, "Regulated Goods")
            } else {
                loadFirstSpinner(sscv_trade_info_q_1, it, "What is your commodity category?")
            }
        })


        //TODO: Extract String resources
        tradeInfoViewModel.getTradeInfoSecondSpinnerContent().observe(this, Observer{
            if(tradeInfoCategory == "Regulated Goods"){
                loadSecondSpinner(sscv_trade_info_q_2, it, "Regulation Type")
            } else {
                loadSecondSpinner(sscv_trade_info_q_2, it, "Select your product")
            }
        })

        tradeInfoViewModel.getTradeInfoThirdSpinnerContent().observe(this, Observer {
            if(tradeInfoCategory != "Regulated Goods"){
                loadThirdSpinner(sscv_trade_info_q_3, it, "Select your product origin")
            }
        })

        tradeInfoViewModel.getTradeInfoFourthSpinnerContent().observe(this, Observer {
            if(tradeInfoCategory != "Regulated Goods"){
                loadFourthSpinner(sscv_trade_info_q_4, it, "Select where you're going")
            }
        })

        tradeInfoViewModel.getTradeInfoFifthSpinnerContent().observe(this, Observer {
            loadFifthSpinner(sscv_trade_info_q_5, it, "Is the value of your goods: ")
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
            }
        }

    }




    private fun buttonSpinnerSetup() {

        categoryListener = View.OnClickListener { v ->
            val b = v as Button

            b.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))


            tradeInfoCategory = b.text.toString()
            tradeInfoViewModel.setFirstSpinnerContent(tradeInfoCategory)

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

                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val country = parent.getItemAtPosition(position) as String

                    if(!country.isNullOrEmpty()){
                        map.forEach mapBreak@{
                            if(it.value == country) {
                                //firstSpinnerSelected = it.key
                                tradeInfoViewModel.setSecondSpinnerContent(it.key)
                                return@mapBreak
                            }
                        }
                    }
                }

            }
            next.setSpinnerListener(listener)

        } else {
            next.addSpinnerContents(spinnerList)

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
        }

        next.addSearchHeader(headerString)
    }

    fun loadSecondSpinner(second: SearchSpinnerCustomView, spinnerList : List<String>, headerString : String) {
        second.visibility = View.VISIBLE

        if(headerString == "Regulation Type") {
            second.addSpinnerContents(spinnerList)

            val secondListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                    val regulatedType = parent!!.selectedItem as String
                    //val regulatedType = second.getSpinnerSelected()
                    val regulatedType = parent.getItemAtPosition(position) as String

                    if(!regulatedType.isNullOrEmpty()){
                        var country = sscv_trade_info_q_1.getSpinnerSelected()
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

                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    product = parent.getItemAtPosition(position) as String

                    if(!product.isNullOrEmpty()){
                       tradeInfoViewModel.setThirdSpinnerContent(language, category, product)
                    }
                }
            }

            second.setSpinnerListener(listener)
        }

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

    private fun loadFifthSpinner(fifth: SearchSpinnerCustomView, spinnerContent: List<String>, headerString: String) {

        fifth.visibility = View.VISIBLE
        fifth.addSpinnerContents(spinnerContent)
        fifth.addSearchHeader(headerString)

        val fifthListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position) {
                    1 -> {}
                    2 -> {}
                }
            }
        }

        fifth.setSpinnerListener(fifthListener)

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

