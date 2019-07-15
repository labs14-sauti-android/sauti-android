package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.SautiApiError
import com.labs.sauti.repository.SautiRepository

class LoginViewModel(private val sautiRepository: SautiRepository) : BaseViewModel() {

    private val loginResponseLiveData = MutableLiveData<LoginResponse>()
    private val errorLiveData = MutableLiveData<SautiApiError>()

    fun getLoginResponseLiveData(): LiveData<LoginResponse> = loginResponseLiveData
    fun getErrorLiveData(): LiveData<SautiApiError> = errorLiveData

    fun login(username: String, password: String) {
        val disposable = sautiRepository.login(username, password).subscribe(
            {
                loginResponseLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(SautiApiError.fromThrowable(it))
            }
        )

        addDisposable(disposable)
    }


    class Factory(private val sautiRepository: SautiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(sautiRepository) as T
        }
    }
}