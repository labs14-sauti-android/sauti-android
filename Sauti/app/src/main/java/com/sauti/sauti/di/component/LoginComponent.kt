package com.sauti.sauti.di.component

import com.sauti.sauti.activity.LoginActivity
import com.sauti.sauti.di.module.LoginModule
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    fun inject(loginActivity: LoginActivity)

}