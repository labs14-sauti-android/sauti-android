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
import com.labs.sauti.model.User
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.view_model.UserViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

// @NOTE: inheritance inject doesn't work. We cannot inject both BaseActivity and its child. Only inject the child
abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected var activityType = ActivityType.MARKET_PRICES

    abstract var userViewModelFactory: UserViewModel.Factory
    private lateinit var userViewModel: UserViewModel

    abstract var sessionSp: SessionSp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()

        if (sessionSp.isAccessTokenValid()) {
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
            ActivityType.MARKET_PRICES -> nav_view.menu.findItem(R.id.nav_prices).isChecked = true

            // TODO test only remove
            ActivityType.SEARCH -> nav_view.menu.findItem(R.id.nav_border).isChecked = true
        }

    }

    fun setBaseContentView(resId: Int) {
        val view = LayoutInflater.from(this).inflate(resId, drawer_layout, false)
        drawer_layout.addView(view, 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_prices -> {
                if (activityType == ActivityType.MARKET_PRICES) return true

                val intent = Intent(this, MarketPricesActivity::class.java)
                startActivity(intent)

                drawer_layout.closeDrawer(GravityCompat.START)
                finish()

                return true
            }
            R.id.nav_border-> {
                // TODO only for testing remove
                if (activityType == ActivityType.SEARCH) return true

                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)

                drawer_layout.closeDrawer(GravityCompat.START)
                finish()

                return true
            }
            R.id.nav_marketplace -> {
                return true
            }
            R.id.nav_rates -> {
                return true
            }
            R.id.nav_tax -> {
                return true
            }
            R.id.nav_log_in_out -> {
                if (sessionSp.isAccessTokenValid()) { // log out
                    // TODO call api logout
                    sessionSp.invalidateToken()
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
}
