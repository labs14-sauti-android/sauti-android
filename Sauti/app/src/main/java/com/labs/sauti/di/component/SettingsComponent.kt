package com.labs.sauti.di.component

import com.labs.sauti.di.module.SettingsModule
import com.labs.sauti.fragment.SettingsFragment
import dagger.Subcomponent

@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent {
    fun inject(settingsFragment: SettingsFragment)
}