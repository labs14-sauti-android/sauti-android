package com.labs.sauti.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.labs.sauti.R

class TradeInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.TRADE_INFO

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_trade_info)


    }



}
