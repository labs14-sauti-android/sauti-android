package com.labs.sauti.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat

import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.fragment.*
import com.labs.sauti.model.User
import com.labs.sauti.view_model.AuthenticationViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.app_bar_base.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
SignInFragment.OnSignInCompletedListener, OnFragmentFullScreenStateChangedListener{

    @Inject
    lateinit var authenticationViewModelFactory: AuthenticationViewModel.Factory

    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var baseFragment: Fragment
    private var appCloseDoubleClickTimerDisposable: Disposable? = null

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
        supportActionBar!!.title = "Dashboard"
        baseFragment = DashboardFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.primary_fragment_container, baseFragment)
            .commit()
        nav_view.menu.findItem(R.id.nav_dashboard).isChecked = true
    }

    override fun onResume() {
        super.onResume()

        authenticationViewModel.getIsSignedInLiveData().observe(this, Observer<Boolean> {
            if (it) {
                nav_view.menu.findItem(R.id.nav_sign_in_out).title = getString(R.string.menu_sign_out)
            } else {
                setUserNavInfoAsLoggedOut()
                nav_view.menu.findItem(R.id.nav_sign_in_out).title = getString(R.string.menu_sign_in)
            }
        })
        authenticationViewModel.isSignedIn()

        // user data
        authenticationViewModel.getUserLiveData().observe(this, Observer<User> {
            nav_view.n_main_t_name.text = it.username ?: ""
        })
        authenticationViewModel.getCurrentUser()

    }

    override fun onDestroy() {
        super.onDestroy()

        appCloseDoubleClickTimerDisposable?.dispose()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                if (replaceFragment(DashboardFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_market_prices -> {
                if (replaceFragment(MarketPriceFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_tax_calculator-> {
                if (replaceFragment(TaxCalculatorFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_trade_info -> {
                if (replaceFragment(TradeInfoFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_exchange_rates -> {
                if (replaceFragment(ExchangeRatesFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_marketplace -> {
                if (replaceFragment(MarketplaceFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_sign_in_out -> {
                if (item.title == getString(R.string.menu_sign_out)) {
                    authenticationViewModel.signOut()

                    item.title = getString(R.string.menu_sign_in)
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
            R.id.nav_report -> {
                if (replaceFragment(ReportFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_help -> {
                if (replaceFragment(HelpFragment::class.java)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1) {
            supportFragmentManager.popBackStack()
            return
        }

        if (!recursivePopBackStack(baseFragment.childFragmentManager)) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                if (appCloseDoubleClickTimerDisposable != null) {
                    super.onBackPressed()
                    return
                }

                appCloseDoubleClickTimerDisposable = Completable.timer(2000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        appCloseDoubleClickTimerDisposable = null
                    }

                Toast.makeText(this, "Press back again to exit the app", Toast.LENGTH_SHORT).show()
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

        nav_view.menu.findItem(R.id.nav_sign_in_out).title = getString(R.string.menu_sign_out)
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

    private fun <T : Fragment> replaceFragment(c: Class<T>): Boolean {
        var shouldReplace = false
        when {
            c.isAssignableFrom(Fragment::class.java) -> throw RuntimeException("Invalid Fragment")
            c.isAssignableFrom(DashboardFragment::class.java) -> {
                if (baseFragment !is DashboardFragment) {
                    shouldReplace = true
                    baseFragment = DashboardFragment.newInstance()
                    toolbar.title = "Dashboard"
                }
            }
            c.isAssignableFrom(MarketPriceFragment::class.java) -> {
                if (baseFragment !is MarketPriceFragment) {
                    shouldReplace = true
                    baseFragment = MarketPriceFragment.newInstance()
                    toolbar.title = "Market Price"

                }
            }
            c.isAssignableFrom(TaxCalculatorFragment::class.java) -> {
                if (baseFragment !is TaxCalculatorFragment) {
                    shouldReplace = true
                    baseFragment = TaxCalculatorFragment.newInstance()
                    toolbar.title = "Tax Calculator"
                }
            }
            c.isAssignableFrom(TradeInfoFragment::class.java) -> {
                if (baseFragment !is TradeInfoFragment) {
                    shouldReplace = true
                    baseFragment = TradeInfoFragment.newInstance()
                    toolbar.title = "Trade Info"
                }
            }
            c.isAssignableFrom(ExchangeRatesFragment::class.java) -> {
                if (baseFragment !is ExchangeRatesFragment) {
                    shouldReplace = true
                    baseFragment = ExchangeRatesFragment.newInstance()
                    toolbar.title = "Exchange Rates"
                }
            }
            c.isAssignableFrom(MarketplaceFragment::class.java) -> {
                if (baseFragment !is MarketplaceFragment) {
                    shouldReplace = true
                    baseFragment = MarketplaceFragment.newInstance()
                    toolbar.title = "Marketplace"
                }
            }
            c.isAssignableFrom(ReportFragment::class.java) -> {
                if (baseFragment !is ReportFragment) {
                    shouldReplace = true
                    baseFragment = ReportFragment.newInstance()
                    toolbar.title = "Report"
                }
            }
            c.isAssignableFrom(HelpFragment::class.java) -> {
                if (baseFragment !is HelpFragment) {
                    shouldReplace = true
                    baseFragment = HelpFragment.newInstance()
                    toolbar.title = "Help"
                }
            }
            else -> throw RuntimeException("Invalid Fragment")
        }

        if (shouldReplace) {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.primary_fragment_container, baseFragment)
                .commit()
        }

        return shouldReplace
    }
}
