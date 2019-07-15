package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.LoginResponse
import com.labs.sauti.model.SautiApiError
import com.labs.sauti.model.User
import com.labs.sauti.repository.SautiRepository

class AuthenticationViewModel(private val sautiRepository: SautiRepository) : BaseViewModel() {

    private val signInResponseLiveData = MutableLiveData<LoginResponse>()
    private val isSignedInLiveData = MutableLiveData<Boolean>()
    private val userLiveData = MutableLiveData<User>()
    private val errorLiveData = MutableLiveData<SautiApiError>()

    fun getLoginResponseLiveData(): LiveData<LoginResponse> = signInResponseLiveData
    fun getIsSignedInLiveData(): LiveData<Boolean> = isSignedInLiveData
    fun getUserLiveData(): LiveData<User> = userLiveData
    fun getErrorLiveData(): LiveData<SautiApiError> = errorLiveData

    fun signIn(username: String, password: String) {
        val disposable = sautiRepository.login(username, password).subscribe(
            {
                signInResponseLiveData.postValue(it)
            },
            {
                // TODO return a more generic error
                errorLiveData.postValue(SautiApiError.fromThrowable(it))
            }
        )

        addDisposable(disposable)
    }

    fun signOut() {
        val disposable = sautiRepository.signOut().subscribe(
            {

            },
            {

            }
        )
        addDisposable(disposable)
    }

    fun isSignedIn() {
        val disposable = sautiRepository.isAccessTokenValid().subscribe(
            {
                isSignedInLiveData.postValue(it)
            },
            {

            }
        )

        addDisposable(disposable)
    }

    fun getCurrentUser() {
        val disposable = sautiRepository.getCurrentUser().subscribe(
            {
                userLiveData.postValue(it)
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
            return AuthenticationViewModel(sautiRepository) as T
        }
    }
}