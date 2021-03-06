package com.labs.sauti.di.component

import com.labs.sauti.activity.BaseActivity
import com.labs.sauti.di.module.AuthenticationModule
import com.labs.sauti.fragment.SignInFragment
import com.labs.sauti.fragment.SignUpFragment
import dagger.Subcomponent

@Subcomponent(modules = [AuthenticationModule::class])
interface AuthenticationComponent {
    fun inject(signInFragment: SignInFragment)
    fun inject(signUpFragment: SignUpFragment)
    fun inject(baseActivity: BaseActivity)

}