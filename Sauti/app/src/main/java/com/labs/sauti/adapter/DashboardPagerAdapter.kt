package com.labs.sauti.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.labs.sauti.fragment.DashboardFavoritesFragment
import com.labs.sauti.fragment.DashboardRecentSearchesFragment

class DashboardPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        private val TAB_TITLES = listOf("Favorites", "Recent Searches")
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> DashboardFavoritesFragment.newInstance()
            1 -> DashboardRecentSearchesFragment.newInstance()
            else -> throw RuntimeException("Unknown tabs position")
        }
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }


}