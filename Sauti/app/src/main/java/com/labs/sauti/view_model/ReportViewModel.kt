package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.ReportForm
import com.labs.sauti.repository.ReportRepository
import com.labs.sauti.view_state.report.BordersViewState
import com.labs.sauti.view_state.report.SubmitReportFormViewState

class ReportViewModel(private val reportRepository: ReportRepository) : BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<Throwable>() }
    private val bordersViewState by lazy { MutableLiveData<BordersViewState>() }
    private val submitReportFormViewState by lazy { MutableLiveData<SubmitReportFormViewState>() }

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getBordersViewState(): LiveData<BordersViewState> = bordersViewState
    fun getSubmitReportFormViewState(): LiveData<SubmitReportFormViewState> = submitReportFormViewState

    fun getBorders() {
        bordersViewState.value = BordersViewState(isLoading = true)
        addDisposable(reportRepository.getBorders().subscribe(
            {
                bordersViewState.postValue(BordersViewState(isLoading = false, borders = it))
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