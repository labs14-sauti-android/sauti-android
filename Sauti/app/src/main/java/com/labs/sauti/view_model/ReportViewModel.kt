package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.ReportForm
import com.labs.sauti.repository.ReportRepository
import com.labs.sauti.view_state.report.BorderNamesViewState
import com.labs.sauti.view_state.report.SubmitReportFormViewState

class ReportViewModel(private val reportRepository: ReportRepository) : BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<Throwable>() }
    private val borderNamesViewState by lazy { MutableLiveData<BorderNamesViewState>() }
    private val submitReportFormViewState by lazy { MutableLiveData<SubmitReportFormViewState>() }

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getBorderNamesViewState(): LiveData<BorderNamesViewState> = borderNamesViewState
    fun getSubmitReportFormViewState(): LiveData<SubmitReportFormViewState> = submitReportFormViewState

    fun getBorders() {
        borderNamesViewState.value = BorderNamesViewState(isLoading = true)
        addDisposable(reportRepository.getBorderNames().subscribe(
            {
                borderNamesViewState.postValue(BorderNamesViewState(isLoading = false, borderNames = it))
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun submitReportForm(reportForm: ReportForm) {
        submitReportFormViewState.value = SubmitReportFormViewState(isLoading = true)
        addDisposable(reportRepository.submitReportForm(reportForm).subscribe(
            {
                submitReportFormViewState.postValue(SubmitReportFormViewState(isLoading = false, isSentSuccessfully = true))
            },
            {
                submitReportFormViewState.postValue(SubmitReportFormViewState(isLoading = false, isSentSuccessfully = false))
            }
        ))
    }

    class Factory(private val reportRepository: ReportRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(reportRepository) as T
        }

    }
}