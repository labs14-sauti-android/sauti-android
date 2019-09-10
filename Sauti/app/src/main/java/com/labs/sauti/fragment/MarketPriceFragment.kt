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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.adapter.RecentMarketPriceAdapter
import com.labs.sauti.databinding.FragmentMarketPriceBinding
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.view_model.MarketPriceViewModel
import kotlinx.android.synthetic.main.fragment_market_price.*
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.max

class MarketPriceFragment : Fragment(), MarketPriceSearchFragment.OnMarketPriceSearchCompletedListener,
OnFragmentFullScreenStateChangedListener {

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var marketPriceViewModelFactory: MarketPriceViewModel.Factory

    private lateinit var marketPriceViewModel: MarketPriceViewModel
    private lateinit var binding: FragmentMarketPriceBinding

    private lateinit var recentMarketPriceAdapter: RecentMarketPriceAdapter

    private var shouldSelectMostRecentMarketPriceView = false
    private var selectedRecentMarketPricesPosition = -1

    private var detailMarketPrice: MarketPrice? = null

    private val networkChangedReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetworkHelper.hasNetworkConnection(context!!)) {
                t_warning_no_network_connection.visibility = View.GONE

                onNetworkConnected(context)
            } else {
                t_warning_no_network_connection.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            detailMarketPrice = it.getParcelable(ARG_MARKET_PRICE)
        }

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

        // set initial market price details
        detailMarketPrice?.let {
            setMarketPriceDetails(it)
            TransitionManager.beginDelayedTransition(fl_fragment_container)
            cl_details.visibility = View.VISIBLE
        }

        context!!.registerReceiver(networkChangedReceiver, IntentFilter().also {
            it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        })

        recentMarketPriceAdapter = RecentMarketPriceAdapter(mutableListOf(),
            object: RecentMarketPriceAdapter.OnRecentMarketPriceClickedListener {
                override fun onRecentMarketPriceClicked(position: Int, recentMarketPrice: MarketPrice) {
                    this@MarketPriceFragment.onRecentMarketPriceClicked(position, recentMarketPrice)
                }
            })

        // setup recycler view
        r_recent_market_prices.layoutManager = GridLayoutManager(context, 2)
        r_recent_market_prices.adapter = recentMarketPriceAdapter

        // error
        marketPriceViewModel.getErrorLiveData().observe(this, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        })

        // recent market prices
        marketPriceViewModel.getRecentMarketPricesViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_recent_market_prices_loading.displayedChild = 1
            } else {
                vs_recent_market_prices_loading.displayedChild = 0

                if (it.recentMarketPrices == null) {
                    vs_recent_market_prices_empty.displayedChild = 1
                } else {
                    it.recentMarketPrices?.let { recentMarketPrices ->

                        if (recentMarketPrices.isEmpty()) {
                            vs_recent_market_prices_empty.displayedChild = 1
                        } else {
                            vs_recent_market_prices_empty.displayedChild = 0
                            handleRecentMarketPrices(recentMarketPrices)

                            if (shouldSelectMostRecentMarketPriceView) {
                                selectedRecentMarketPricesPosition = 0
                            }
                        }

                        shouldSelectMostRecentMarketPriceView = false
                    }
                }
            }
        })
        marketPriceViewModel.getRecentMarketPrices()

        // signed in user
        marketPriceViewModel.getSignedInUserViewState().observe(this, Observer {
            if (!it.isLoading) {
                if (it.user?.userId != null) {
                    ll_favorite.visibility = View.VISIBLE
                    detailMarketPrice?.let { marketPrice->
                        marketPriceViewModel.isFavoriteMarketPriceSearch(
                            NetworkHelper.hasNetworkConnection(context!!),
                            marketPrice.country!!,
                            marketPrice.market!!,
                            marketPrice.productCat!!,
                            marketPrice.product!!
                        )
                    }
                } else{
                    ll_favorite.visibility = View.GONE
                }
            }
        })
        marketPriceViewModel.getSignedInUser(NetworkHelper.hasNetworkConnection(context!!))

        marketPriceViewModel.getIsFavoriteMarketPriceSearchViewState().observe(this, Observer {
            if (it.isLoading) {
                ll_favorite.isEnabled = false
            } else {
                ll_favorite.isEnabled = true
                if (it.isFavorite) {
                    i_favorite.setImageResource(R.drawable.ic_star_filled)
                } else {
                    i_favorite.setImageResource(R.drawable.ic_star_empty)
                }
            }
        })

        // search click
        b_search.setOnClickListener {
            openMarketPriceSearchFragment()
        }
    }

    private fun onNetworkConnected(context: Context) {
        // update market prices if connected to wifi or has 4g
        if (NetworkHelper.hasNetworkConnection(context) &&
            (NetworkHelper.hasTransport(context, NetworkCapabilities.TRANSPORT_WIFI) ||
                    NetworkHelper.getNetworkClass(context) == NetworkHelper.CLASS_4G)) {
            marketPriceViewModel.updateMarketPrices()
        }

        marketPriceViewModel.syncFavoriteMarketPriceSearches()
    }

    private fun openMarketPriceSearchFragment() {
        val marketPriceSearchFragment = MarketPriceSearchFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container, marketPriceSearchFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setMarketPriceDetails(marketPrice: MarketPrice) {
        detailMarketPrice = marketPrice

        val productAtMarketSStr = SpannableString("${marketPrice.product} at ${marketPrice.market}")
        productAtMarketSStr.setSpan(UnderlineSpan(), 0, productAtMarketSStr.length, 0)
        t_details_product_at_market.text = productAtMarketSStr
        val decimalFormat = DecimalFormat("#,##0")
        val wholesaleStr = decimalFormat.format(marketPrice.wholesale ?: 0.0)
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

        t_nearby_markets.text = buildString {
            if (marketPrice.nearbyMarketplaceNames.isEmpty()) {
                append("None")
                return@buildString
            }

            marketPrice.nearbyMarketplaceNames.forEach {
                append(it)
                append(", ")
            }
            setLength(max(0, length - 2))
        }

        val signedInUserViewState = marketPriceViewModel.getSignedInUserViewState().value
        signedInUserViewState?.let {
            if (it.user?.userId != null) {
                marketPriceViewModel.isFavoriteMarketPriceSearch(
                    NetworkHelper.hasNetworkConnection(context!!),
                    marketPrice.country ?: "",
                    marketPrice.market ?: "",
                    marketPrice.productCat ?: "",
                    marketPrice.product ?: ""
                )
            } else {
                ll_favorite.visibility = View.GONE
            }
        }

        ll_favorite.setOnClickListener {
            marketPriceViewModel.toggleFavorite(
                NetworkHelper.hasNetworkConnection(context!!),
                marketPrice.country ?: "",
                marketPrice.market ?: "",
                marketPrice.productCat ?: "",
                marketPrice.product ?: ""
            )
        }
    }

    private fun handleRecentMarketPrices(recentMarketPrices: List<MarketPrice>) {
        recentMarketPriceAdapter.setRecentMarketPrices(recentMarketPrices)
    }

    private fun onRecentMarketPriceClicked(position: Int, recentMarketPrice: MarketPrice) {
        if (selectedRecentMarketPricesPosition == -1) { // nothing was selected
            setMarketPriceDetails(recentMarketPrice)
            TransitionManager.beginDelayedTransition(fl_fragment_container)
            cl_details.visibility = View.VISIBLE
            selectedRecentMarketPricesPosition = position
            return
        }

        if (position == selectedRecentMarketPricesPosition) { // selected was clicked
            TransitionManager.beginDelayedTransition(fl_fragment_container)
            if (cl_details.visibility == View.VISIBLE) {
                cl_details.visibility = View.GONE
            } else {
                cl_details.visibility = View.VISIBLE
            }
        } else { // other item was clicked
            setMarketPriceDetails(recentMarketPrice)
            TransitionManager.beginDelayedTransition(fl_fragment_container)
            cl_details.visibility = View.VISIBLE
        }

        selectedRecentMarketPricesPosition = position
    }

    override fun onMarketPriceSearchCompleted(marketPrice: MarketPrice) {
        cl_details.visibility = View.VISIBLE
        setMarketPriceDetails(marketPrice)

        marketPriceViewModel.getRecentMarketPricesInCache()
        selectedRecentMarketPricesPosition = -1 // unselect
        shouldSelectMostRecentMarketPriceView = true
    }

    override fun onAttach(context: Context) {
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
        private const val ARG_MARKET_PRICE = "arg_market_price"

        @JvmStatic
        fun newInstance(marketPrice: MarketPrice?) =
            MarketPriceFragment().apply {
                arguments = Bundle().apply {
                    marketPrice?.let {
                        putParcelable(ARG_MARKET_PRICE, it)
                    }
                }
            }
    }
}
