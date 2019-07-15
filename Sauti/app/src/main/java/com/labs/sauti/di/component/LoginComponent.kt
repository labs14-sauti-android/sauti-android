package com.labs.sauti.di.component

import com.labs.sauti.activity.LoginActivity
import com.labs.sauti.di.module.LoginModule
import com.labs.sauti.fragment.SignInFragment
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    fun inject(loginActivity: LoginActivity)
    fun inject(signInFragment: SignInFragment)

}