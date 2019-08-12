package com.labs.sauti.repository

import com.labs.sauti.model.ReportForm
import io.reactivex.Completable
import io.reactivex.Single

interface ReportRepository {
    fun getBorders(): Single<MutableList<String>>
    fun submitReportForm(reportForm: ReportForm): Completable
}