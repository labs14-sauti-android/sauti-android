package com.labs.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var sessionSp: SessionSp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        // @NOTE: inheritance inject doesn't work. We cannot inject both BaseActivity and its child
        // so we just get the userViewModelFactory directly from the UserComponent
        val userComponent = (applicationContext as SautiApp).getUserComponent()

        userViewModel = ViewModelProviders.of(this, userComponent.userViewModelFactory()).get(UserViewModel::class.java)

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
    }

    fun setBaseContentView(resId: Int) {
        val view = LayoutInflater.from(this).inflate(resId, drawer_layout, false)
        drawer_layout.addView(view, 0)
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

    private fun setUserNavInfoAsLoggedOut() {
        nav_view.getHeaderView(0).n_main_t_name.text = getString(R.string.not_logged_in)
    }
}
