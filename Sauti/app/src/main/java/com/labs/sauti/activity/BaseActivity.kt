package com.labs.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.User
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.view_model.UserViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

// @NOTE: inheritance inject doesn't work. We cannot inject both BaseActivity and its child. Only inject the child
open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected var activityType = ActivityType.MARKET_PRICES

    private lateinit var userViewModel: UserViewModel

    private val injectWrapper = InjectWrapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        (applicationContext as SautiApp).getMainComponent().inject(injectWrapper)

        userViewModel = ViewModelProviders.of(this, injectWrapper.userViewModelFactory).get(UserViewModel::class.java)

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()

        if (injectWrapper.sessionSp.isAccessTokenValid()) {
            nav_view.menu.findItem(R.id.nav_log_in_out).title = getString(R.string.menu_log_out)
        } else {
            setUserNavInfoAsLoggedOut()
            nav_view.menu.findItem(R.id.nav_log_in_out).title = getString(R.string.menu_log_in)
        }

        userViewModel.getUserLiveData().observe(this, Observer<User> {
            nav_view.n_main_t_name.text = it.username ?: ""
        })

        userViewModel.getCurrentUser()

        when (activityType) {
            ActivityType.MARKET_PRICES -> nav_view.menu.findItem(R.id.nav_market_prices).isChecked = true
            ActivityType.TAX_CALCULATOR -> nav_view.menu.findItem(R.id.nav_tax_calculator).isChecked = true
            ActivityType.TRADE_INFO -> nav_view.menu.findItem(R.id.nav_trade_info).isChecked = true
            ActivityType.DASHBOARD -> nav_view.menu.findItem(R.id.nav_dashboard).isChecked = true
        }

    }

    fun setBaseContentView(resId: Int) {
        val view = LayoutInflater.from(this).inflate(resId, drawer_layout, false)
        drawer_layout.addView(view, 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_market_prices -> {
                if (activityType == ActivityType.MARKET_PRICES) return true

                val intent = Intent(this, MarketPricesActivity::class.java)
                startActivity(intent)

                drawer_layout.closeDrawer(GravityCompat.START)
                finish()

                return true
            }
            R.id.nav_tax_calculator-> {
                if (activityType == ActivityType.TAX_CALCULATOR) return true

                val intent = Intent(this, TaxCalculatorActivity::class.java)
                startActivity(intent)

                drawer_layout.closeDrawer(GravityCompat.START)
                finish()

                return true
            }
            R.id.nav_trade_info -> {
                if (activityType == ActivityType.TRADE_INFO) return true

                val intent = Intent(this, TradeInfoActivity::class.java)
                startActivity(intent)

                drawer_layout.closeDrawer(GravityCompat.START)
                finish()

                return true
            }
            R.id.nav_exchange_rates -> {
                return true
            }
            R.id.nav_dashboard -> {
                if (activityType == ActivityType.DASHBOARD) return true

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)

                drawer_layout.closeDrawer(GravityCompat.START)
                finish()

                return true
            }
            R.id.nav_log_in_out -> {
                if (injectWrapper.sessionSp.isAccessTokenValid()) { // log out
                    // TODO call api logout
                    injectWrapper.sessionSp.invalidateToken()
                    item.title = getString(R.string.menu_log_in)

                    setUserNavInfoAsLoggedOut()

                    drawer_layout.closeDrawer(GravityCompat.START)

                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                return true
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setUserNavInfoAsLoggedOut() {
        nav_view.getHeaderView(0).n_main_t_name.text = getString(R.string.not_logged_in)
    }

    class InjectWrapper {
        @Inject
        lateinit var userViewModelFactory: UserViewModel.Factory

        @Inject
        lateinit var sessionSp: SessionSp
    }

}
