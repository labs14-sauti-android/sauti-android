package com.labs.sauti.di.module

import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_model.AuthenticationViewModel
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

    @Provides
    fun provideAuthenticationViewModelFactory(userRepository: UserRepository): AuthenticationViewModel.Factory {
        return AuthenticationViewModel.Factory(userRepository)
    }

}