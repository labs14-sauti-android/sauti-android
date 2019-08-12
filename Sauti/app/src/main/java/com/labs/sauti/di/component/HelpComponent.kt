package com.labs.sauti.di.component

import com.labs.sauti.di.module.HelpModule
import com.labs.sauti.fragment.HelpFragment
import dagger.Subcomponent

@Subcomponent(modules = [HelpModule::class])
interface HelpComponent {
    fun inject(helpFragment: HelpFragment)
}