package com.sauti.sauti.di.component

import com.sauti.sauti.activity.SearchActivity
import com.sauti.sauti.di.module.UserModule
import dagger.Subcomponent

@Subcomponent(modules = [UserModule::class])
interface UserComponent {
    fun inject(searchActivity: SearchActivity)
}