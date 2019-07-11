package com.sauti.sauti.di.module

import com.sauti.sauti.repository.SautiRepository
import com.sauti.sauti.view_model.LoginViewModel
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun provideLoginViewModelFactory(sautiRepository: SautiRepository): LoginViewModel.Factory {
        return LoginViewModel.Factory(sautiRepository)
    }

}