package com.labs.sauti.di.module

import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.view_model.UserViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule {

    @Provides
    @Singleton
    fun provideUserViewModelFactory(sautiRepository: SautiRepository, sessionSp: SessionSp): UserViewModel.Factory {
        return UserViewModel.Factory(sautiRepository, sessionSp)
    }

}