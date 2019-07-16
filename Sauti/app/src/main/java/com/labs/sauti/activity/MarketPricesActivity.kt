package com.labs.sauti.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.fragment.MarketPriceSearchFragment
import com.labs.sauti.model.MarketPrice
import com.labs.sauti.view_model.MarketPricesViewModel
import kotlinx.android.synthetic.main.activity_market_prices.*
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import javax.inject.Inject

class MarketPricesActivity : BaseActivity(), MarketPriceSearchFragment.OnSearchCompletedListener {

    companion object {
        private const val MAX_MARKET_RECENT_PRICE_SHOWN = 2
    }

    @Inject
    lateinit var marketPricesViewModelFactory: MarketPricesViewModel.Factory

    private lateinit var marketPricesViewModel: MarketPricesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.MARKET_PRICES

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_market_prices)

        // inject
        (applicationContext as SautiApp).getMarketPricesComponent().inject(this)
        marketPricesViewModel = ViewModelProviders.of(this, marketPricesViewModelFactory).get(MarketPricesViewModel::class.java)

        ll_details.visibility = View.GONE

        marketPricesViewModel.getRecentMarketPricesLiveData().observe(this, Observer<MutableList<MarketPrice>> {
            ll_recent_market_prices.removeAllViews()

            for ((index, marketPrice) in it.withIndex()) {
                if (index == MAX_MARKET_RECENT_PRICE_SHOWN) break

                val recentMarketPriceView = LayoutInflater.from(this).inflate(R.layout.item_recent_market_price, ll_recent_market_prices, false)

                recentMarketPriceView.t_recent_product_at_market.text = "${marketPrice.product} at ${marketPrice.market}"
                recentMarketPriceView.t_recent_wholesale.text = "Wholesale: ${marketPrice.wholesale} ${marketPrice.currency}/1Kg"
                recentMarketPriceView.t_recent_retail.text = "Retail: ${marketPrice.retail} ${marketPrice.currency}/1Kg"
                recentMarketPriceView.t_recent_updated.text = marketPrice.date
                recentMarketPriceView.t_recent_source.text = "Where does source come from?"

                recentMarketPriceView.setOnClickListener {
                    ll_details.visibility = View.VISIBLE
                    setMarketPriceViewDetails(marketPrice)
                }

                ll_recent_market_prices.addView(recentMarketPriceView)

            }
        })
        marketPricesViewModel.getRecentMarketPrices()

        b_search.setOnClickListener {
            val marketPriceSearchFragment = MarketPriceSearchFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fl_root, marketPriceSearchFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    private fun setMarketPriceViewDetails(marketPrice: MarketPrice) {
        t_details_product_at_market.text = "${marketPrice.product} at ${marketPrice.market}"
        t_details_wholesale.text = "Wholesale: ${marketPrice.wholesale} ${marketPrice.currency}/1Kg"
        t_details_retail.text = "Retail: ${marketPrice.retail} ${marketPrice.currency}/1Kg"
        t_details_updated.text = marketPrice.date
        t_details_source.text = "Where does source come from?"
    }

    override fun onSearchCompleted(marketPrice: MarketPrice) {
        ll_details.visibility = View.VISIBLE
        marketPricesViewModel.getRecentMarketPrices()
    }
}
