package com.labs.sauti.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.databinding.FragmentMarketPriceBinding
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.view_model.MarketPriceViewModel
import kotlinx.android.synthetic.main.fragment_market_price.*
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import java.text.DecimalFormat
import javax.inject.Inject

class MarketPriceFragment : Fragment(), MarketPriceSearchFragment.OnMarketPriceSearchCompletedListener,
OnFragmentFullScreenStateChangedListener {

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var marketPriceViewModelFactory: MarketPriceViewModel.Factory

    private lateinit var marketPriceViewModel: MarketPriceViewModel
    private lateinit var binding: FragmentMarketPriceBinding

    private var shouldSelectMostRecentMarketPriceView = false
    private var selectedRecentMarketPriceView: View? = null

    private val networkChangedReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetworkHelper.hasNetworkConnection(context!!)) {
                t_warning_no_network_connection.visibility = View.GONE
            } else {
                t_warning_no_network_connection.visibility = View.VISIBLE
            }
        }
    }

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

        context!!.registerReceiver(networkChangedReceiver, IntentFilter().also {
            it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        })

        tryUpdatingMarketPrices()

        ll_details.visibility = View.GONE

        marketPriceViewModel.getRecentMarketPricesViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_recent_market_prices_loading.displayedChild = 1
            } else {
                vs_recent_market_prices_loading.displayedChild = 0

                ll_recent_market_prices.removeAllViews()

                if (it.recentMarketPrices == null) {
                    vs_recent_market_prices_empty.displayedChild = 1
                } else {
                    it.recentMarketPrices?.let { recentMarketPrices ->

                        if (recentMarketPrices.isEmpty()) {
                            vs_recent_market_prices_empty.displayedChild = 1
                        } else {
                            vs_recent_market_prices_empty.displayedChild = 0
                            handleRecentMarketPrices(recentMarketPrices)
                        }

                        shouldSelectMostRecentMarketPriceView = false
                    }
                }
            }
        })
        marketPriceViewModel.getRecentMarketPrices()

        b_search.setOnClickListener {
            openMarketPriceSearchFragment()
        }
    }

    private fun tryUpdatingMarketPrices() {
        if (NetworkHelper.hasNetworkConnection(context!!) &&
            (NetworkHelper.hasTransport(context!!, NetworkCapabilities.TRANSPORT_WIFI) ||
                    NetworkHelper.getNetworkClass(context!!) == NetworkHelper.CLASS_4G)) {
            marketPriceViewModel.updateMarketPrices()
        }
    }

    private fun openMarketPriceSearchFragment() {
        val marketPriceSearchFragment = MarketPriceSearchFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container, marketPriceSearchFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setMarketPriceDetails(marketPrice: MarketPrice) {
        val productAtMarketSStr = SpannableString("${marketPrice.product} at ${marketPrice.market}")
        productAtMarketSStr.setSpan(UnderlineSpan(), 0, productAtMarketSStr.length, 0)
        t_details_product_at_market.text = productAtMarketSStr
        val decimalFormat = DecimalFormat("#,##0.00")
        val wholesaleStr = decimalFormat.format(marketPrice.wholesale)
        t_details_wholesale.text = "Wholesale: $wholesaleStr ${marketPrice.currency}/1Kg"
        if (marketPrice.retail != null && marketPrice.retail!! > 0.0) {
            t_details_retail.visibility = View.VISIBLE

            val retailStr = decimalFormat.format(marketPrice.retail!!)
            t_details_retail.text = "Retail: $retailStr ${marketPrice.currency}/1Kg"
        } else {
            t_details_retail.visibility = View.GONE
        }
        t_details_updated.text = "Updated: ${marketPrice.date?.substring(0, 10)}"
        t_details_source.text = "Source: EAGC-RATIN" // TODO
    }

    private fun handleRecentMarketPrices(recentMarketPrices: List<MarketPrice>) {
        recentMarketPrices.forEachIndexed recentMarketPricesBreak@{ index, recentMarketPrice ->
            if (index > MAX_RECENT_ITEMS) {
                return@recentMarketPricesBreak
            }

            val itemView = LayoutInflater.from(context!!).inflate(R.layout.item_recent_market_price, ll_recent_market_prices, false)
            ll_recent_market_prices.addView(itemView)

            if (index == 0 && shouldSelectMostRecentMarketPriceView) selectedRecentMarketPriceView = itemView

            itemView.t_recent_product_at_market.text = "${recentMarketPrice.product} at ${recentMarketPrice.market}"
            val decimalFormat = DecimalFormat("#,##0.00")
            val wholesaleStr = decimalFormat.format(recentMarketPrice.wholesale)
            itemView.t_recent_wholesale.text = "Wholesale: $wholesaleStr ${recentMarketPrice.currency}/1Kg"
            if (recentMarketPrice.retail != null && recentMarketPrice.retail!! > 0.0) {
                itemView.t_recent_retail.visibility = View.VISIBLE

                val retailStr = decimalFormat.format(recentMarketPrice.retail!!)
                itemView.t_recent_retail.text = "Retail: $retailStr ${recentMarketPrice.currency}/1Kg"
            } else {
                itemView.t_recent_retail.visibility = View.GONE
            }
            itemView.t_recent_updated.text = "Updated: ${recentMarketPrice.date?.substring(0, 10)}"
            itemView.t_recent_source.text = "Source: EAGC-RATIN" // TODO

            itemView.setOnClickListener {
                if (selectedRecentMarketPriceView == null) {
                    setMarketPriceDetails(recentMarketPrice)
                    TransitionManager.beginDelayedTransition(fl_fragment_container)
                    ll_details.visibility = View.VISIBLE
                    selectedRecentMarketPriceView = it
                    return@setOnClickListener
                }

                if (it == selectedRecentMarketPriceView) {
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

                selectedRecentMarketPriceView = it
            }
        }
    }

    override fun onMarketPriceSearchCompleted(marketPrice: MarketPrice) {
        ll_details.visibility = View.VISIBLE
        setMarketPriceDetails(marketPrice)

        marketPriceViewModel.getRecentMarketPricesInCache()
        shouldSelectMostRecentMarketPriceView = true
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

    override fun onDestroy() {
        super.onDestroy()

        context!!.unregisterReceiver(networkChangedReceiver)
    }

    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(isFullScreen)
    }

    companion object {

        private const val MAX_RECENT_ITEMS = 2

        @JvmStatic
        fun newInstance() =
            MarketPriceFragment()
    }
}
