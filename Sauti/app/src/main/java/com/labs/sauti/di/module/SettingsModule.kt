package com.labs.sauti.di.module

import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.view_model.SettingsViewModel
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    fun provideSettingsViewModelFactory(sautiRepository: SautiRepository): SettingsViewModel.Factory {
        return SettingsViewModel.Factory(sautiRepository)
    }

}