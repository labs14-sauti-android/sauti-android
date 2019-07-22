package com.labs.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.fragment.OnFragmentFullScreenStateChangedListener
import com.labs.sauti.fragment.SignInFragment
import com.labs.sauti.model.User
import com.labs.sauti.view_model.AuthenticationViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.app_bar_base.*
import kotlinx.android.synthetic.main.content_base.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

// @NOTE: inheritance inject doesn't work. We cannot inject both BaseActivity and its child. Only inject the child
open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
SignInFragment.OnSignInCompletedListener, OnFragmentFullScreenStateChangedListener{
    protected var activityType = ActivityType.MARKET_PRICES

    private lateinit var authenticationViewModel: AuthenticationViewModel

    private val injectWrapper = InjectWrapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        (applicationContext as SautiApp).getAuthenticationComponent().inject(injectWrapper)

        authenticationViewModel = ViewModelProviders.of(this, injectWrapper.authenticationViewModelFactory).get(AuthenticationViewModel::class.java)

        nav_view.setNavigationItemSelectedListener(this)

        // hamburger
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onResume() {
        super.onResume()

        authenticationViewModel.getIsSignedInLiveData().observe(this, Observer<Boolean> {
            if (it) {
                nav_view.menu.findItem(R.id.nav_log_in_out).title = getString(R.string.menu_log_out)
            } else {
                setUserNavInfoAsLoggedOut()
                nav_view.menu.findItem(R.id.nav_log_in_out).title = getString(R.string.menu_log_in)
            }
        })
        authenticationViewModel.isSignedIn()

        // user data
        authenticationViewModel.getUserLiveData().observe(this, Observer<User> {
            nav_view.n_main_t_name.text = it.username ?: ""
        })
        authenticationViewModel.getCurrentUser()

        when (activityType) {
            ActivityType.MARKET_PRICES -> nav_view.menu.findItem(R.id.nav_market_prices).isChecked = true
            ActivityType.TAX_CALCULATOR -> nav_view.menu.findItem(R.id.nav_tax_calculator).isChecked = true
            ActivityType.TRADE_INFO -> nav_view.menu.findItem(R.id.nav_trade_info).isChecked = true
            ActivityType.EXCHANGE_RATES -> nav_view.menu.findItem(R.id.nav_exchange_rates).isChecked = true
            ActivityType.DASHBOARD -> nav_view.menu.findItem(R.id.nav_dashboard).isChecked = true
        }

    }

    fun setBaseContentView(resId: Int) {
        val view = LayoutInflater.from(this).inflate(resId, primary_fragment_container, false)
        primary_fragment_container.addView(view)
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
                if (activityType == ActivityType.EXCHANGE_RATES) return true

                val intent = Intent(this, ExchangeRatesActivity::class.java)
                startActivity(intent)

                drawer_layout.closeDrawer(GravityCompat.START)
                finish()

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
                if (item.title == getString(R.string.menu_log_out)) {
                    authenticationViewModel.signOut()

                    item.title = getString(R.string.menu_log_in)
                    setUserNavInfoAsLoggedOut()

                    drawer_layout.closeDrawer(GravityCompat.START)

                } else {
                    val signInFragment = SignInFragment.newInstance()

                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, signInFragment)
                        .addToBackStack(null)
                        .commit()
                }

                return true
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // TODO implement double backpress to close the app
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size != 0) {
            super.onBackPressed()
            return
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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

    private fun setUserNavInfoAsLoggedOut() {
        nav_view.getHeaderView(0).n_main_t_name.text = getString(R.string.not_logged_in)
    }

    override fun onSignInCompleted() {
        // user data
        authenticationViewModel.getUserLiveData().observe(this, Observer<User> {
            nav_view.n_main_t_name.text = it.username ?: ""
        })
        authenticationViewModel.getCurrentUser()

        nav_view.menu.findItem(R.id.nav_log_in_out).title = getString(R.string.menu_log_out)
    }

    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        if (isFullScreen) {
            toolbar.visibility = View.GONE
            drawer_layout.closeDrawer(GravityCompat.START)
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            toolbar.visibility = View.VISIBLE
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    class InjectWrapper {
        @Inject
        lateinit var authenticationViewModelFactory: AuthenticationViewModel.Factory
    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.primary_fragment_container, fragment)
            .commit()
    }



}
