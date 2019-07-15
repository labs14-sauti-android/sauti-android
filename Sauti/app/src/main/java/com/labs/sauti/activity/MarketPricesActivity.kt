package com.labs.sauti.activity

import android.os.Bundle
import com.labs.sauti.R
import com.labs.sauti.SautiApp

class MarketPricesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.MARKET_PRICES

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_market_prices)

        // inject
        (applicationContext as SautiApp).getMarketPricesComponent().inject(this)

    }


}
