package com.labs.sauti.di.component

import com.labs.sauti.di.module.UserModule
import com.labs.sauti.view_model.UserViewModel
import dagger.Subcomponent

@Subcomponent(modules = [UserModule::class])
interface UserComponent {
    fun userViewModelFactory(): UserViewModel.Factory
}