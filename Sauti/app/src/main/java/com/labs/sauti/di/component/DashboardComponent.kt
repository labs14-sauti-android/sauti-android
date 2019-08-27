package com.labs.sauti.di.component

import com.labs.sauti.di.module.DashboardModule
import com.labs.sauti.fragment.DashboardFavoritesFragment
import com.labs.sauti.fragment.DashboardRecentSearchesFragment
import dagger.Subcomponent

@Subcomponent(modules = [DashboardModule::class])
interface DashboardComponent {
    fun inject(dashboardFavoritesFragment: DashboardFavoritesFragment)
    fun inject(dashboardRecentSearchesFragment: DashboardRecentSearchesFragment)
}