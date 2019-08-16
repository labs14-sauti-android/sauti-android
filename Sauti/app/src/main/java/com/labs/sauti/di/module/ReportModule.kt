package com.labs.sauti.di.module

import com.labs.sauti.repository.ReportRepository
import com.labs.sauti.view_model.ReportViewModel
import dagger.Module
import dagger.Provides

@Module
class ReportModule {

    @Provides
    fun provideReportViewModelFactory(reportRepository: ReportRepository): ReportViewModel.Factory {
        return ReportViewModel.Factory(reportRepository)
    }
}

