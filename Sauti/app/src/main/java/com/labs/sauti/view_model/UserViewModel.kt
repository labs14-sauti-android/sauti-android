package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.SautiApiError
import com.labs.sauti.model.User
import com.labs.sauti.repository.SautiRepository
import com.labs.sauti.sp.SessionSp
import retrofit2.HttpException

class UserViewModel(private val sautiRepository: SautiRepository, private val sessionSp: SessionSp): BaseViewModel() {

    private val userLiveData = MutableLiveData<User>()
    private val errorLiveData = MutableLiveData<SautiApiError>()

    fun getUserLiveData(): LiveData<User> = userLiveData
    fun getErrorLiveData(): LiveData<SautiApiError> = errorLiveData

    fun getCurrentUser() {
        if (sessionSp.isAccessTokenValid()) {
            val disposable = sautiRepository.getCurrentUser(sessionSp.getAccessToken()).subscribe(
                {
                    userLiveData.postValue(it)
                },
                {
                    errorLiveData.postValue(SautiApiError.fromThrowable(it))
                }
            )

            addDisposable(disposable)
        }
    }

    class Factory(private val sautiRepository: SautiRepository, private val sessionSp: SessionSp) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(sautiRepository, sessionSp) as T
        }
    }

}