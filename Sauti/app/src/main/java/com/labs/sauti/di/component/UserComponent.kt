package com.labs.sauti.di.component

import com.labs.sauti.activity.BaseActivity
import com.labs.sauti.activity.SearchActivity
import com.labs.sauti.di.module.UserModule
import dagger.Subcomponent

@Subcomponent(modules = [UserModule::class])
interface UserComponent {
    fun inject(searchActivity: SearchActivity)
    fun inject(baseActivity: BaseActivity)
}