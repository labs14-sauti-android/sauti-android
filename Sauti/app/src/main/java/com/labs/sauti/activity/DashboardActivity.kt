package com.labs.sauti.activity

import android.os.Bundle
import com.labs.sauti.R

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.DASHBOARD

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_dashboard)

    }

}
