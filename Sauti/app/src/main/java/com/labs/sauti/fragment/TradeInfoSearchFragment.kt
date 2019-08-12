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
import kotlin.math.log


class TradeInfoSearchFragment : Fragment() {

    private var onTradeSearchCompletedListener : onTradeInfoSearchCompletedListener? = null
    private var onFragmentFullScreenStateChangedListener : OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory
    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var categoryListener : View.OnClickListener
    private lateinit var spinnerListener: AdapterView.OnItemSelectedListener

    private lateinit var buttonList : List<Button>
    private lateinit var searchSpinnerList : List<SearchSpinnerCustomView>


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

        val lang = SettingsSp(context!!).getSelectedLanguage()
        tradeInfoViewModel.setLanguage(lang)

        tradeInfoViewModel.setFirstSpinnerContent()

        tradeInfoViewModel.getTradeInfoFirstSpinnerContent().observe(this, Observer {
            val category = tradeInfoViewModel.getTradeInfoCategory().value as String
            if(it.isNotEmpty()) {
                loadNextSpinner(sscv_trade_info_q_1, it, category, category)
            } else {
                loadNextSpinner(sscv_trade_info_q_1, listOf("fat"), category, category )
            }
        })


        b_trade_info_search.setOnClickListener {

        }

//        tradeInfoViewModel.loadFirstSpinnerContent()



        //TODO: Testing SpinnerCustomView logic
        //loadNextSpinner(sscv_trade_info_q_1)

    }

    fun observeTradeInfoViewModel() {

        //Border Procedures
        // Category -> Product -> Going Where -> Origin Made -> Value
        //fun loadFirstSp


    }



    private fun buttonSpinnerSetup() {

//        spinnerListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//            }
//
//        }
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

        b_trade_info_search.isEnabled = false
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(parentFragment is onTradeInfoSearchCompletedListener) {
            onFragmentFullScreenStateChangedListener = parentFragment as OnFragmentFullScreenStateChangedListener
            onFragmentFullScreenStateChangedListener!!.onFragmetFullScreenStateChanged(true)
        }  else {
            throw RuntimeException("parentFragment must implement OnFragmentFullScreenStateChangedListener")
        }
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

    interface onTradeInfoSearchCompletedListener {
        fun onTradeInfoSearchCompleted(tradeInfo: TradeInfo)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TradeInfoSearchFragment()
    }

    fun loadNextSpinner(next: SearchSpinnerCustomView, spinnerList : List<String>, headerString : String?, spinCat: String) {
        next.visibility = View.VISIBLE
        next.addSpinnerContents(spinnerList)
        if (headerString != null) {
            next.addSearchHeader(headerString)
        } else {
            next.addSearchHeader("Bananas")
        }

        //next.progressBarSVisibility()
    }
}

