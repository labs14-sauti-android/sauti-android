package com.labs.sauti.activity

import android.graphics.Color
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
import com.google.android.material.snackbar.Snackbar
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.fragment.*
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.model.market_price.MarketPrice
import com.labs.sauti.view_model.AuthenticationViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.app_bar_base.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.RuntimeException

class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
SignInFragment.OnSignInCompletedListener, OnFragmentFullScreenStateChangedListener,
SignInFragment.OpenSignUpListener, SignUpFragment.OpenSignInListener,
DashboardFragment.OnReplaceFragmentListener, SettingsFragment.OnLanguageChangedListener,
DashboardFavoritesFragment.OnFavoriteClickListener{

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
        supportActionBar!!.title = "Sauti"
        baseFragment = DashboardFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.primary_fragment_container, baseFragment)
            .commit()
        nav_view.menu.findItem(R.id.nav_dashboard).isChecked = true

        // authentication error
        authenticationViewModel.getErrorLiveData().observe(this, Observer {
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_SHORT).show()
        })

        // user data
        authenticationViewModel.getSignedInUserViewState().observe(this, Observer {
            if (it.isLoading) {
                // TODO loading
            } else {
                if (it.user?.userId != null) {
                    nav_view.getHeaderView(0).n_main_t_name.text = it.user!!.username
                    nav_view.menu.findItem(R.id.nav_sign_in_out).title = getString(R.string.menu_sign_out)
                } else {
                    nav_view.getHeaderView(0).n_main_t_name.text = ""
                    nav_view.menu.findItem(R.id.nav_sign_in_out).title = getString(R.string.menu_sign_in)
                }
            }
        })

        authenticationViewModel.getSignOutViewState().observe(this, Observer {
            if (it.isLoading) {
                // TODO loading
            } else {
                if (it.isSuccess) {
                    Snackbar.make(findViewById(android.R.id.content), "Signed out", Snackbar.LENGTH_SHORT).show()
                    nav_view.menu.findItem(R.id.nav_sign_in_out).title = getString(R.string.menu_sign_in)
                    setUserNavInfoAsLoggedOut()

                    // restart activity
                    finish()
                    startActivity(intent)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        authenticationViewModel.getSignedInUser(NetworkHelper.hasNetworkConnection(this))
    }

    override fun onDestroy() {
        super.onDestroy()

        appCloseDoubleClickTimerDisposable?.dispose()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                if (replaceFragment(DashboardFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_market_prices -> {
                if (replaceFragment(MarketPriceFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_tax_calculator-> {
                if (replaceFragment(TaxCalculatorFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_trade_info -> {
                if (replaceFragment(TradeInfoFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_exchange_rates -> {
                if (replaceFragment(ExchangeRateFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_marketplace -> {
                if (replaceFragment(MarketplaceFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_sign_in_out -> {
                if (item.title == getString(R.string.menu_sign_out)) {
                    authenticationViewModel.signOut()

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
                if (replaceFragment(ReportFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }
            R.id.nav_help -> {
                if (replaceFragment(HelpFragment::class.java, null)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }

                return true
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1) { // pop signin/signout, about, or settings
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
            R.id.action_about -> {
                val aboutFragment = AboutFragment.newInstance()

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, aboutFragment)
                    .addToBackStack(null)
                    .commit()
                true
            }
            R.id.action_settings -> {
                val settingsFragment = SettingsFragment.newInstance()

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, settingsFragment)
                    .addToBackStack(null)
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUserNavInfoAsLoggedOut() {
        nav_view.getHeaderView(0).n_main_t_name.text = ""
    }

    override fun onSignInCompleted() {
        authenticationViewModel.getSignedInUser(NetworkHelper.hasNetworkConnection(this))

        // restart activity
        finish()
        startActivity(intent)
    }

    override fun openSignUp() {
        val signUpFragment = SignUpFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, signUpFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun openSignIn() {
        val signInFragment = SignInFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, signInFragment)
            .addToBackStack(null)
            .commit()
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

    override fun <T : Fragment> onReplaceFragment(c: Class<T>) {
        replaceFragment(c, null)
    }

    private fun <T : Fragment> replaceFragment(c: Class<T>, data: Any?): Boolean {
        var shouldReplace = false
        when {
            c.isAssignableFrom(Fragment::class.java) -> throw RuntimeException("Invalid Fragment")
            c.isAssignableFrom(DashboardFragment::class.java) -> {
                if (baseFragment !is DashboardFragment) {
                    shouldReplace = true
                    baseFragment = DashboardFragment.newInstance()
                    toolbar.title = "Sauti"
                    nav_view.menu.findItem(R.id.nav_dashboard).isChecked = true
                }
            }
            c.isAssignableFrom(MarketPriceFragment::class.java) -> {
                if (baseFragment !is MarketPriceFragment) {
                    shouldReplace = true
                    baseFragment = MarketPriceFragment.newInstance(data as MarketPrice?)
                    toolbar.title = "Market Price"
                    nav_view.menu.findItem(R.id.nav_market_prices).isChecked = true
                }
            }
            c.isAssignableFrom(TaxCalculatorFragment::class.java) -> {
                if (baseFragment !is TaxCalculatorFragment) {
                    shouldReplace = true
                    baseFragment = TaxCalculatorFragment.newInstance()
                    toolbar.title = "Tax Calculator"
                    nav_view.menu.findItem(R.id.nav_tax_calculator).isChecked = true
                }
            }
            c.isAssignableFrom(TradeInfoFragment::class.java) -> {
                if (baseFragment !is TradeInfoFragment) {
                    shouldReplace = true
                    baseFragment = TradeInfoFragment.newInstance()
                    toolbar.title = "Trade Info"
                    nav_view.menu.findItem(R.id.nav_trade_info).isChecked = true
                }
            }
            c.isAssignableFrom(ExchangeRateFragment::class.java) -> {
                if (baseFragment !is ExchangeRateFragment) {
                    shouldReplace = true
                    baseFragment = ExchangeRateFragment.newInstance(data as ExchangeRateConversionResult?)
                    toolbar.title = "Exchange Rates"
                    nav_view.menu.findItem(R.id.nav_exchange_rates).isChecked = true
                }
            }
            c.isAssignableFrom(MarketplaceFragment::class.java) -> {
                if (baseFragment !is MarketplaceFragment) {
                    shouldReplace = true
                    baseFragment = MarketplaceFragment.newInstance()
                    toolbar.title = "Marketplace"
                    nav_view.menu.findItem(R.id.nav_marketplace).isChecked = true
                }
            }
            c.isAssignableFrom(ReportFragment::class.java) -> {
                if (baseFragment !is ReportFragment) {
                    shouldReplace = true
                    baseFragment = ReportFragment.newInstance()
                    toolbar.title = "Report"
                    nav_view.menu.findItem(R.id.nav_report).isChecked = true
                }
            }
            c.isAssignableFrom(HelpFragment::class.java) -> {
                if (baseFragment !is HelpFragment) {
                    shouldReplace = true
                    baseFragment = HelpFragment.newInstance()
                    toolbar.title = "Help"
                    nav_view.menu.findItem(R.id.nav_help).isChecked = true
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

    override fun onLanguageChanged() {
        finish()
        startActivity(intent)
    }

    override fun onFavoriteClick(favorite: Any) {
        when (favorite) {
            is MarketPrice -> replaceFragment(MarketPriceFragment::class.java, favorite)
            is ExchangeRateConversionResult -> replaceFragment(ExchangeRateFragment::class.java, favorite)
            else -> throw RuntimeException("Unhandled favorite type")
        }
    }
}
