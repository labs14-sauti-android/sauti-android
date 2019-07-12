package com.sauti.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.sauti.sauti.R
import com.sauti.sauti.SautiApp
import com.sauti.sauti.model.User
import com.sauti.sauti.sp.SessionSp
import com.sauti.sauti.view_model.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var userViewModelFactory: UserViewModel.Factory

    private lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var sessionSp: SessionSp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // inject user
        val userComponent = (applicationContext as SautiApp).getUserComponent()
        userComponent.inject(this)

        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

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

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_prices -> {
                return true
            }
            R.id.nav_border-> {
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

                    finish()
                }

                return true
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
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
}
