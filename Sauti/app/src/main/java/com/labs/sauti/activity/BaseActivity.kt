package com.labs.sauti.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat

import androidx.drawerlayout.widget.DrawerLayout

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.fragment.*
import com.labs.sauti.model.User
import com.labs.sauti.view_model.AuthenticationViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.app_bar_base.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
SignInFragment.OnSignInCompletedListener, OnFragmentFullScreenStateChangedListener{

    @Inject
    lateinit var authenticationViewModelFactory: AuthenticationViewModel.Factory

    private lateinit var authenticationViewModel: AuthenticationViewModel

    private lateinit var baseFragment: BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        (applicationContext as SautiApp).getAuthenticationComponent().inject(this)

        authenticationViewModel = ViewModelProviders.of(this, authenticationViewModelFactory).get(AuthenticationViewModel::class.java)

        nav_view.setNavigationItemSelectedListener(this)

        // hamburger
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // initial base fragment
        toolbar.title = "Dashboard"
        baseFragment = DashboardFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.primary_fragment_container, baseFragment)
            .commit()

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

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                replaceBaseFragment(BaseFragment.Type.DASHBOARD)
                drawer_layout.closeDrawer(GravityCompat.START)

                return true
            }
            R.id.nav_market_prices -> {
                replaceBaseFragment(BaseFragment.Type.MARKET_PRICE)
                drawer_layout.closeDrawer(GravityCompat.START)

                return true
            }
            R.id.nav_tax_calculator-> {
                replaceBaseFragment(BaseFragment.Type.TAX_CALCULATOR)
                drawer_layout.closeDrawer(GravityCompat.START)

                return true
            }
            R.id.nav_trade_info -> {
                replaceBaseFragment(BaseFragment.Type.TRADE_INFO)
                drawer_layout.closeDrawer(GravityCompat.START)

                return true
            }
            R.id.nav_exchange_rates -> {
                replaceBaseFragment(BaseFragment.Type.EXCHANGE_RATES)
                drawer_layout.closeDrawer(GravityCompat.START)

                return true
            }
            R.id.nav_marketplace -> {
                replaceBaseFragment(BaseFragment.Type.MARKETPLACE)
                drawer_layout.closeDrawer(GravityCompat.START)

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
                        .replace(R.id.fragment_container, signInFragment)
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
    // TODO close the login pop up instead of the nav drawer first
    override fun onBackPressed() {
        if (!recursivePopBackStack(baseFragment.childFragmentManager)) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
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

    private fun replaceBaseFragment(baseFragmentType: BaseFragment.Type) {
        var shouldReplace = false
        when (baseFragmentType) {
            BaseFragment.Type.DASHBOARD -> {
                if (baseFragment.getFragmentType() != BaseFragment.Type.DASHBOARD) {
                    shouldReplace = true
                    baseFragment = DashboardFragment.newInstance()
                    toolbar.title = "Dashboard"
                }
            }
            BaseFragment.Type.MARKET_PRICE -> {
                if (baseFragment.getFragmentType() != BaseFragment.Type.MARKET_PRICE) {
                    shouldReplace = true
                    baseFragment = MarketPriceFragment.newInstance()
                    toolbar.title = "Market Price"
                }
            }
            BaseFragment.Type.TAX_CALCULATOR -> {
                if (baseFragment.getFragmentType() != BaseFragment.Type.TAX_CALCULATOR) {
                    shouldReplace = true
                    baseFragment = TaxCalculatorFragment.newInstance()
                    toolbar.title = "Tax Calculator"
                }
            }
            BaseFragment.Type.TRADE_INFO -> {
                if (baseFragment.getFragmentType() != BaseFragment.Type.TRADE_INFO) {
                    shouldReplace = true
                    baseFragment = TradeInfoFragment.newInstance()
                    toolbar.title = "Trade Info"
                }
            }
            BaseFragment.Type.EXCHANGE_RATES -> {
                if (baseFragment.getFragmentType() != BaseFragment.Type.EXCHANGE_RATES) {
                    shouldReplace = true
                    baseFragment = ExchangeRatesFragment.newInstance()
                    toolbar.title = "Exchange Rates"
                }
            }
            BaseFragment.Type.MARKETPLACE -> {
                if (baseFragment.getFragmentType() != BaseFragment.Type.MARKETPLACE) {
                    shouldReplace = true
                    baseFragment = MarketplaceFragment.newInstance()
                    toolbar.title = "Marketplace"
                }
            }
        }

        if (shouldReplace) {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.primary_fragment_container, baseFragment)
                .commit()
        }
    }

    /** Pop the child of the fragment in the fragmentManager*/
    private fun recursivePopBackStack(fragmentManager: FragmentManager): Boolean {
        for (fragment in fragmentManager.fragments) {
            if (fragment != null && fragment.isVisible) {
                if (recursivePopBackStack(fragment.childFragmentManager)) {
                    return true
                }
            }
        }

        // pop front most
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
            return true
        }

        return false
    }
}
