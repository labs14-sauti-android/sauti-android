package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.model.ReportForm
import io.reactivex.Completable
import io.reactivex.Single

class ReportRepositoryImpl(sautiApiService: SautiApiService) : ReportRepository {
    override fun getBorders(): Single<MutableList<String>> {
        return Single.just((0..0).map {"KEN"}.toMutableList())
    }

    override fun submitReportForm(reportForm: ReportForm): Completable {
        return Completable.complete()
    }
}