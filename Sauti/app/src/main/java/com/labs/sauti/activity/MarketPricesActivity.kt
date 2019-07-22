package com.labs.sauti.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.labs.sauti.R
import com.labs.sauti.fragment.MarketPriceFragment

class MarketPricesActivity : BaseActivity() {

    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        activityType = ActivityType.MARKET_PRICES

        super.onCreate(savedInstanceState)
        setBaseContentView(R.layout.activity_market_prices)

        fragment = MarketPriceFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_root, fragment)
            .commit()

    }

    override fun onBackPressed() {
        if (!recursivePopBackStack(fragment.childFragmentManager)) {
            super.onBackPressed()
        }
    }

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
