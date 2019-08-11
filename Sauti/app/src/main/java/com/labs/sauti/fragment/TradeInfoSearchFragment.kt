package com.labs.sauti.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.view_model.TradeInfoViewModel
import com.labs.sauti.views.SearchSpinnerCustomView
import kotlinx.android.synthetic.main.fragment_market_price_search.*
import kotlinx.android.synthetic.main.fragment_trade_info_search.*
import javax.inject.Inject


class TradeInfoSearchFragment : Fragment() {

    private var onTradeSearchCompletedListener : onTradeInfoSearchCompletedListener? = null
    private var onFragmentFullScreenStateChangedListener : OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory
    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var categoryListener : View.OnClickListener

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
        }

        tradeInfoViewModel = ViewModelProviders.of(this, tradeInfoViewModelFactory)
            .get(TradeInfoViewModel::class.java)

        categoryListener = View.OnClickListener { v ->

            val b = v as Button
            b.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))

            buttonList.forEach {
                if(it.id != b.id) {
                    it.background.setTint(ContextCompat.getColor(context!!, R.color.colorButtonResponse))
                }
            }

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

        //Places all buttons in a list, setss clicklisteners and disable search button.
        buttonSetup()

        //TODO: Testing SpinnerCustomView logic
        loadNextSpinner(sscv_trade_info_q_1)

    }



    private fun buttonSetup() {
        buttonList = mutableListOf(b_trade_info_procedures, b_trade_info_documents, b_trade_info_agencies, b_trade_info_regulated)

        b_trade_info_procedures.setOnClickListener(categoryListener)
        b_trade_info_documents.setOnClickListener(categoryListener)
        b_trade_info_agencies.setOnClickListener(categoryListener)
        b_trade_info_regulated.setOnClickListener(categoryListener)

        b_trade_info_search.isEnabled = false
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

    fun loadNextSpinner(next: SearchSpinnerCustomView) {
        next.progressBarSVisibility()
        next.addSearchHeader("What are you looking for?")
        next.addSpinnerContents(listOf(" ",
            "Border Procedures",
            "Required Documents",
            "Border Agencies",
            "Tax Calculator",
            "Regulated Goods")
        )
    }
}

