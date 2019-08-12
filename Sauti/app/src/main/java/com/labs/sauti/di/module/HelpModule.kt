package com.labs.sauti.di.module

import com.labs.sauti.repository.HelpRepository
import com.labs.sauti.view_model.HelpViewModel
import dagger.Module
import dagger.Provides

@Module
class HelpModule {

    @Provides
    fun provideHelpViewModelFactory(helpRepository: HelpRepository): HelpViewModel.Factory {
        return HelpViewModel.Factory(helpRepository)
    }
}