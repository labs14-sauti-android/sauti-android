package com.labs.sauti.activity

import android.os.Bundle
import com.labs.sauti.R

class TaxCalculatorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.TAX_CALCULATOR

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_tax_calculator)

    }

}
