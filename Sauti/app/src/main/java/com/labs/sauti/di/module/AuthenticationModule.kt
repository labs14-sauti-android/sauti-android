package com.labs.sauti.di.module

import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_model.AuthenticationViewModel
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

    @Provides
    fun provideAuthenticationViewModelFactory(sautiRepository: SautiRepository): AuthenticationViewModel.Factory {
        return AuthenticationViewModel.Factory(sautiRepository)
    }

}