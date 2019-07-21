package com.labs.sauti.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.fragment.MarketPriceFragment
import com.labs.sauti.fragment.MarketPriceSearchFragment
import com.labs.sauti.model.MarketPriceData
import com.labs.sauti.view_model.MarketPricesViewModel
import kotlinx.android.synthetic.main.activity_market_prices.*
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import javax.inject.Inject

class MarketPricesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.MARKET_PRICES

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_market_prices)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_root, MarketPriceFragment.newInstance())
            .commit()

    }

}
