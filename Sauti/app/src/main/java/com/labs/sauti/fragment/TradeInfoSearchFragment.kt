package com.labs.sauti.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.TradeInfoData
import com.labs.sauti.view_model.TradeInfoViewModel
import com.labs.sauti.views.SearchSpinnerCustomView
import kotlinx.android.synthetic.main.fragment_trade_info_search.*
import javax.inject.Inject


class TradeInfoSearchFragment : Fragment() {

    private var onTradeSearchCompletedListener : onTradeInfoSearchCompletedListener? = null
    private var onFragmentFullScreenStateChangedListener : OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory
    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            firebaseAnalytics = FirebaseAnalytics.getInstance(it)
            (it.applicationContext as SautiApp).getTradeInfoComponent().inject(this)
        }
        
        tradeInfoViewModel = ViewModelProviders.of(this, tradeInfoViewModelFactory)
            .get(TradeInfoViewModel::class.java)
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

        //TODO: Testing SpinnerCustomView logic
        loadNextSpinner(sscv_trade_info)

    }

    interface onTradeInfoSearchCompletedListener {
        fun onTradeInfoSearchCompleted(tradeInfo: TradeInfoData)
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

