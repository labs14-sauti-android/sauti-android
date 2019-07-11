package com.sauti.sauti.di.module

import com.sauti.sauti.repository.SautiRepository
import com.sauti.sauti.sp.SessionSp
import com.sauti.sauti.view_model.UserViewModel
import dagger.Module
import dagger.Provides

@Module
class UserModule {

    @Provides
    fun provideUserViewModelFactory(sautiRepository: SautiRepository, sessionSp: SessionSp): UserViewModel.Factory {
        return UserViewModel.Factory(sautiRepository, sessionSp)
    }

}