package com.labs.sauti.di.component

import com.labs.sauti.di.module.ReportModule
import com.labs.sauti.fragment.ReportFragment
import dagger.Subcomponent

@Subcomponent(modules = [ReportModule::class])
interface ReportComponent {
    fun inject(reportFragment: ReportFragment)
}