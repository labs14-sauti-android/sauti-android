package com.labs.sauti.fragment

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.databinding.FragmentMarketPriceBinding
import com.labs.sauti.model.MarketPriceData
import com.labs.sauti.model.RecentMarketPriceData
import com.labs.sauti.view_model.MarketPriceViewModel
import kotlinx.android.synthetic.main.fragment_market_price.*
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import javax.inject.Inject

// TODO 5 weeks old max market price
class MarketPriceFragment : Fragment(), MarketPriceSearchFragment.OnMarketPriceSearchCompletedListener,
OnFragmentFullScreenStateChangedListener {

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var marketPriceViewModelFactory: MarketPriceViewModel.Factory

    private lateinit var marketPriceViewModel: MarketPriceViewModel
    private lateinit var binding: FragmentMarketPriceBinding

    private val recentMarketPriceRootViews = mutableListOf<View>()
    private var selectedRecentMarketPriceRootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inject
        (context!!.applicationContext as SautiApp).getMarketPriceComponent().inject(this)
        marketPriceViewModel = ViewModelProviders.of(this, marketPriceViewModelFactory).get(MarketPriceViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return DataBindingUtil.inflate<FragmentMarketPriceBinding>(inflater, R.layout.fragment_market_price, container, false).also {
            binding = it
            binding.marketPriceViewModel = marketPriceViewModel
            binding.lifecycleOwner = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ll_details.visibility = View.GONE

        // TODO remove RecentMarketPrice
//        ll_recent_market_prices.removeAllViews()
//        marketPriceViewModel.getRecentMarketPricesLiveData().observe(this, Observer {
//            ll_recent_market_prices.removeAllViews()
//
//            handleRecentMarketPrices(it)
//        })
//        marketPriceViewModel.getRecentMarketPrices()

        ll_recent_market_prices.children.iterator().forEach {
            recentMarketPriceRootViews.add(it)
        }

        marketPriceViewModel.getSearchRecentMarketPricesLiveData().observe(this, Observer {

            vs_recent_market_prices_loading.displayedChild = 0

            recentMarketPriceRootViews.forEachIndexed { index, view ->
                if (index < it.size) {
                    val recentMarketPrice = it[index]
                    view.t_recent_product_at_market.text = "${recentMarketPrice.product} at ${recentMarketPrice.market}"
                    view.t_recent_wholesale.text = "Wholesale: ${recentMarketPrice.wholesale} ${recentMarketPrice.currency}/1Kg"
                    view.t_recent_retail.text = "Retail: ${recentMarketPrice.retail} ${recentMarketPrice.currency}/1Kg"
                    view.t_recent_updated.text = "Updated: ${recentMarketPrice.date?.substring(0, 10)}"
                    view.t_recent_source.text = "Source: EAGC-RATIN" // TODO

                    view.setOnClickListener {
                        if (selectedRecentMarketPriceRootView == null) {
                            setMarketPriceDetails(recentMarketPrice)
                            TransitionManager.beginDelayedTransition(fl_fragment_container)
                            ll_details.visibility = View.VISIBLE
                            selectedRecentMarketPriceRootView = it
                            return@setOnClickListener
                        }

                        if (it == selectedRecentMarketPriceRootView) {
                            TransitionManager.beginDelayedTransition(fl_fragment_container)
                            if (ll_details.visibility == View.VISIBLE) {
                                ll_details.visibility = View.GONE
                            } else {
                                ll_details.visibility = View.VISIBLE
                            }
                        } else {
                            setMarketPriceDetails(recentMarketPrice)
                            if (ll_details.visibility == View.GONE) {
                                TransitionManager.beginDelayedTransition(fl_fragment_container)
                                ll_details.visibility = View.VISIBLE
                            }
                        }

                        selectedRecentMarketPriceRootView = it
                    }
                } else {
                    view.t_recent_product_at_market.text = ""
                    view.t_recent_wholesale.text = ""
                    view.t_recent_retail.text = ""
                    view.t_recent_updated.text = ""
                    view.t_recent_source.text = ""

                    view.setOnClickListener(null)
                }
            }
        })
        vs_recent_market_prices_loading.displayedChild = 1
        marketPriceViewModel.searchRecentMarketPrices()

        b_search.setOnClickListener {
            openMarketPriceSearchFragment()
        }
    }

    // TODO remove RecentMarketPrice
    private fun handleRecentMarketPrices(recentMarketPrices: MutableList<RecentMarketPriceData>) {
        for ((index, recentMarketPrice) in recentMarketPrices.withIndex()) {
            if (index == MAX_MARKET_RECENT_PRICE_SHOWN) break

            val recentMarketPriceView = LayoutInflater.from(context).inflate(R.layout.item_recent_market_price, ll_recent_market_prices, false)

            recentMarketPriceView.t_recent_product_at_market.text = "${recentMarketPrice.product} at ${recentMarketPrice.market}"
            recentMarketPriceView.t_recent_wholesale.text = "Wholesale: ${recentMarketPrice.wholesale} ${recentMarketPrice.currency}/1Kg"
            recentMarketPriceView.t_recent_retail.text = "Retail: ${recentMarketPrice.retail} ${recentMarketPrice.currency}/1Kg"
            recentMarketPriceView.t_recent_updated.text = "Updated: ${recentMarketPrice.date?.substring(0, 10)}"
            recentMarketPriceView.t_recent_source.text = "Source: EAGC-RATIN" // TODO

            recentMarketPriceView.setOnClickListener {
                ll_details.visibility = View.VISIBLE
                t_details_product_at_market.text = "${recentMarketPrice.product} at ${recentMarketPrice.market}"
                t_details_wholesale.text = "Wholesale: ${recentMarketPrice.wholesale} ${recentMarketPrice.currency}/1Kg"
                t_details_retail.text = "Retail: ${recentMarketPrice.retail} ${recentMarketPrice.currency}/1Kg"
                t_details_updated.text = "Updated: ${recentMarketPrice.date?.substring(0, 10)}"
                t_details_source.text = "Source: EAGC-RATIN" // TODO
            }

            ll_recent_market_prices.addView(recentMarketPriceView)
        }
    }

    private fun openMarketPriceSearchFragment() {
        val marketPriceSearchFragment = MarketPriceSearchFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container, marketPriceSearchFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setMarketPriceDetails(marketPrice: MarketPriceData) {
        val productAtMarketSStr = SpannableString("${marketPrice.product} at ${marketPrice.market}")
        productAtMarketSStr.setSpan(UnderlineSpan(), 0, productAtMarketSStr.length, 0)
        t_details_product_at_market.text = productAtMarketSStr
        t_details_wholesale.text = "Wholesale: ${marketPrice.wholesale} ${marketPrice.currency}/1Kg"
        t_details_retail.text = "Retail: ${marketPrice.retail} ${marketPrice.currency}/1Kg"
        t_details_updated.text = "Updated: ${marketPrice.date?.substring(0, 10)}"
        t_details_source.text = "Source: EAGC-RATIN" // TODO
    }

    override fun onMarketPriceSearchCompleted(marketPrice: MarketPriceData) {
        ll_details.visibility = View.VISIBLE
        setMarketPriceDetails(marketPrice)

        marketPriceViewModel.searchRecentMarketPriceInCache()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = context
        } else {
            throw RuntimeException("Context must implement OnFragmentFullScreenStateChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onFragmentFullScreenStateChangedListener = null
    }

    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(isFullScreen)
    }

    companion object {
        private const val MAX_MARKET_RECENT_PRICE_SHOWN = 2

        @JvmStatic
        fun newInstance() =
            MarketPriceFragment()
    }
}
