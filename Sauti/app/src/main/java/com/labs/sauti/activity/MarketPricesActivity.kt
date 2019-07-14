package com.labs.sauti.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.view_model.UserViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_market_prices.*
import javax.inject.Inject

class MarketPricesActivity : BaseActivity() {

    @Inject
    override lateinit var userViewModelFactory: UserViewModel.Factory

    @Inject
    override lateinit var sessionSp: SessionSp

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.MARKET_PRICES

        val marketPricesComponent = (applicationContext as SautiApp).getMarketPricesComponent()
        marketPricesComponent.inject(this)

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_market_prices)

        // hamburger
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}
