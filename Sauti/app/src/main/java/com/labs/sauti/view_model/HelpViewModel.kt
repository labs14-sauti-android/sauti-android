package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.repository.HelpRepository
import com.labs.sauti.view_state.help.IncorrectInformationViewState

class HelpViewModel(private val helpRepository: HelpRepository) : BaseViewModel() {

    private val incorrectInfomationViewState by lazy { MutableLiveData<IncorrectInformationViewState>() }

    fun getIncorrectInformationViewState(): LiveData<IncorrectInformationViewState> = incorrectInfomationViewState

    fun submitIncorrectInformation(incorrectInformation: String) {
        incorrectInfomationViewState.value = IncorrectInformationViewState(isLoading = true)
        addDisposable(helpRepository.submitIncorrectInformation(incorrectInformation).subscribe(
            {
                incorrectInfomationViewState.postValue(IncorrectInformationViewState(isLoading = false, isSentSuccessfully = true))
            },
            {
                incorrectInfomationViewState.postValue(IncorrectInformationViewState(isLoading = false, isSentSuccessfully = false))
            }
        ))
    }

    class Factory(private val helpRepository: HelpRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HelpViewModel(helpRepository) as T
        }
    }

}