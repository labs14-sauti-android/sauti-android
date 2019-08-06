package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.repository.SautiRepository

class SettingsViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<Throwable>() }
    private val selectedLanguageLiveData by lazy { MutableLiveData<String>() }

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getSelectedLanguageLiveData(): LiveData<String> = selectedLanguageLiveData

    fun getSelectedLanguage() {
        addDisposable(sautiRepository.getSelectedLanguage().subscribe(
            {
                selectedLanguageLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    class Factory(private val sautiRepository: SautiRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(sautiRepository) as T
        }
    }
}