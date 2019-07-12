package com.labs.sauti.di.module

import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_model.LoginViewModel
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun provideLoginViewModelFactory(sautiRepository: SautiRepository): LoginViewModel.Factory {
        return LoginViewModel.Factory(sautiRepository)
    }

}