package com.labs.sauti.activity

import android.os.Bundle
import com.labs.sauti.R

class ExchangeRatesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.EXCHANGE_RATES

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_exchange_rates)

    }
}
