package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.*
import com.labs.sauti.repository.UserRepository

class AuthenticationViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    // TODO lazy. view state

    private val signUpResponseLiveData by lazy { MutableLiveData<SignUpResponse>() }
    private val signInResponseLiveData = MutableLiveData<SignInResponse>()
    private val signOutLiveData = MutableLiveData<Any?>()
    private val isSignedInLiveData = MutableLiveData<Boolean>()
    private val userLiveData = MutableLiveData<User>()
    private val errorLiveData = MutableLiveData<SautiApiError>()

    fun getSignUpResponseLiveData(): LiveData<SignUpResponse> = signUpResponseLiveData
    fun getSignInResponseLiveData(): LiveData<SignInResponse> = signInResponseLiveData
    fun getSignOutLiveData(): LiveData<Any?> = signOutLiveData
    fun getIsSignedInLiveData(): LiveData<Boolean> = isSignedInLiveData
    fun getUserLiveData(): LiveData<User> = userLiveData
    fun getErrorLiveData(): LiveData<SautiApiError> = errorLiveData

    fun signUp(signUpRequest: SignUpRequest) {
        addDisposable(userRepository.signUp(signUpRequest).subscribe(
            {
                signUpResponseLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(SautiApiError.fromThrowable(it))
            }
        ))
    }

    fun signIn(username: String, password: String) {
        addDisposable(userRepository.signIn(username, password).subscribe(
            {
                signInResponseLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(SautiApiError.fromThrowable(it))
            }
        ))
    }

    fun signOut() {
        addDisposable(userRepository.signOut().subscribe(
            {
                signOutLiveData.postValue(null)
            },
            {
                errorLiveData.postValue(SautiApiError.fromThrowable(it))
            }
        ))
    }

    fun isSignedIn() {
        addDisposable(userRepository.isAccessTokenValid().subscribe(
            {
                isSignedInLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(SautiApiError.fromThrowable(it))
            }
        ))
    }

    fun getCurrentUser() {
        addDisposable(userRepository.getCurrentUser().subscribe(
            {
                userLiveData.postValue(it)
            },
            {
                errorLiveData.postValue(SautiApiError.fromThrowable(it))
            }
        ))
    }

    class Factory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AuthenticationViewModel(userRepository) as T
        }
    }
}