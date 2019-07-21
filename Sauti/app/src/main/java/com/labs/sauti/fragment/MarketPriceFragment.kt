package com.labs.sauti.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.MarketPriceData
import com.labs.sauti.model.RecentMarketPriceData
import com.labs.sauti.view_model.MarketPricesViewModel
import kotlinx.android.synthetic.main.fragment_market_price.*
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import javax.inject.Inject

class MarketPriceFragment : Fragment(), MarketPriceSearchFragment.OnMarketPriceSearchCompletedListener {

    @Inject
    lateinit var marketPricesViewModelFactory: MarketPricesViewModel.Factory

    private lateinit var marketPricesViewModel: MarketPricesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inject
        (context!!.applicationContext as SautiApp).getMarketPricesComponent().inject(this)
        marketPricesViewModel = ViewModelProviders.of(this, marketPricesViewModelFactory).get(MarketPricesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ll_details.visibility = View.GONE

        ll_recent_market_prices.removeAllViews()
        marketPricesViewModel.getRecentMarketPricesLiveData().observe(this, Observer {
            ll_recent_market_prices.removeAllViews()

            handleRecentMarketPrices(it)
        })
        marketPricesViewModel.getRecentMarketPrices()

        b_search.setOnClickListener {
            openMarketPriceSearchFragment()
        }
    }

    private fun handleRecentMarketPrices(recentMarketPrices: MutableList<RecentMarketPriceData>) {
        for ((index, recentMarketPrice) in recentMarketPrices.withIndex()) {
            if (index == MAX_MARKET_RECENT_PRICE_SHOWN) break

            val recentMarketPriceView = LayoutInflater.from(context).inflate(R.layout.item_recent_market_price, ll_recent_market_prices, false)

            recentMarketPriceView.t_recent_product_at_market.text = "${recentMarketPrice.product} at ${recentMarketPrice.market}"
            recentMarketPriceView.t_recent_wholesale.text = "Wholesale: ${recentMarketPrice.wholesale} ${recentMarketPrice.currency}/1Kg"
            recentMarketPriceView.t_recent_retail.text = "Retail: ${recentMarketPrice.retail} ${recentMarketPrice.currency}/1Kg"
            recentMarketPriceView.t_recent_updated.text = recentMarketPrice.date
            recentMarketPriceView.t_recent_source.text = "Where does source come from?"

            recentMarketPriceView.setOnClickListener {
                ll_details.visibility = View.VISIBLE
                t_details_product_at_market.text = "${recentMarketPrice.product} at ${recentMarketPrice.market}"
                t_details_wholesale.text = "Wholesale: ${recentMarketPrice.wholesale} ${recentMarketPrice.currency}/1Kg"
                t_details_retail.text = "Retail: ${recentMarketPrice.retail} ${recentMarketPrice.currency}/1Kg"
                t_details_updated.text = recentMarketPrice.date
                t_details_source.text = "Where does source come from?"
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

    override fun onMarketPriceSearchCompleted(marketPrice: MarketPriceData) {
        ll_details.visibility = View.VISIBLE
        t_details_product_at_market.text = "${marketPrice.product} at ${marketPrice.market}"
        t_details_wholesale.text = "Wholesale: ${marketPrice.wholesale} ${marketPrice.currency}/1Kg"
        t_details_retail.text = "Retail: ${marketPrice.retail} ${marketPrice.currency}/1Kg"
        t_details_updated.text = marketPrice.date
        t_details_source.text = "Where does source come from?"

        marketPricesViewModel.getRecentMarketPrices()
    }

    companion object {
        private const val MAX_MARKET_RECENT_PRICE_SHOWN = 2

        @JvmStatic
        fun newInstance() =
            MarketPriceFragment()
    }
}
