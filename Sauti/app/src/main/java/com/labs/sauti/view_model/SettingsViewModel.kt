package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.repository.SettingsRepository

class SettingsViewModel(private val settingsRepository: SettingsRepository): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<Throwable>() }
    private val selectedLanguageLiveData by lazy { MutableLiveData<String>() }

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getSelectedLanguageLiveData(): LiveData<String> = selectedLanguageLiveData

    fun getSelectedLanguage() {
        addDisposable(settingsRepository.getSelectedLanguage().subscribe(
            {
                selectedLanguageLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    fun setSelectedLanguage(language: String) {
        addDisposable(settingsRepository.setSelectedLanguage(language)
            .subscribe(
            {
                selectedLanguageLiveData.postValue(settingsRepository.getSelectedLanguage().blockingGet())
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    class Factory(private val settingsRepository: SettingsRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
    }
}