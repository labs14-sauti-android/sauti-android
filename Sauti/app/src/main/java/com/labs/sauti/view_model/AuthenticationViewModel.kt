package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.authentication.SignUpRequest
import com.labs.sauti.model.authentication.User
import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_state.authentication.*

class AuthenticationViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<Throwable>() }
    private val signUpViewState by lazy { MutableLiveData<SignUpViewState>() }
    private val signInViewState by lazy { MutableLiveData<SignInViewState>() }
    private val signOutViewState by lazy { MutableLiveData<SignOutViewState>() }
    private val isSignedInViewState by lazy { MutableLiveData<IsSignedInViewState>() }
    private val signedInUserViewState by lazy { MutableLiveData<SignedInUserViewState>() }

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getSignUpViewState(): LiveData<SignUpViewState> = signUpViewState
    fun getSignInViewState(): LiveData<SignInViewState> = signInViewState
    fun getSignOutViewState(): LiveData<SignOutViewState> = signOutViewState
    fun getIsSignedInViewState(): LiveData<IsSignedInViewState> = isSignedInViewState
    fun getSignedInUserViewState(): LiveData<SignedInUserViewState> = signedInUserViewState

    fun signUp(signUpRequest: SignUpRequest) {
        signUpViewState.value = SignUpViewState(isLoading = true)
        addDisposable(userRepository.signUp(signUpRequest).subscribe(
            {
                signUpViewState.postValue(SignUpViewState(isLoading = false, isSuccess = true))
            },
            {
                signUpViewState.postValue(SignUpViewState(isLoading = false, isSuccess = false))
                errorLiveData.postValue(it)
            }
        ))
    }

    fun signIn(username: String, password: String) {
        signInViewState.value = SignInViewState(isLoading = true)
        addDisposable(userRepository.signIn(username, password).subscribe(
            {
                signInViewState.postValue(SignInViewState(isLoading = false, isSuccess = true))
            },
            {
                signInViewState.postValue(SignInViewState(isLoading = false, isSuccess = false))
                errorLiveData.postValue(it)
            }
        ))
    }

    fun signOut() {
        signOutViewState.value = SignOutViewState(isLoading = true)
        addDisposable(userRepository.signOut().subscribe(
            {
                signOutViewState.postValue(SignOutViewState(isLoading = false, isSuccess = true))
            },
            {
                signOutViewState.postValue(SignOutViewState(isLoading = false, isSuccess = false))
                errorLiveData.postValue(it)
            }
        ))
    }

    fun isSignedIn() {
        isSignedInViewState.value = IsSignedInViewState(isLoading = true)
        addDisposable(userRepository.isAccessTokenValid().subscribe(
            {
                isSignedInViewState.postValue(IsSignedInViewState(isLoading = false, isSignedIn = it))
            },
            {
                isSignedInViewState.postValue(IsSignedInViewState(isLoading = false))
                errorLiveData.postValue(it)
            }
        ))
    }

    fun getSignedInUser() {
        signedInUserViewState.value = SignedInUserViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser()
            .map {
                User(
                    it.id,
                    it.username,
                    it.phoneNumber,
                    it.firstName,
                    it.lastName,
                    it.location,
                    it.gender
                )
            }
            .subscribe(
            {
                signedInUserViewState.postValue(SignedInUserViewState(isLoading = false, user = it))
            },
            {
                signedInUserViewState.postValue(SignedInUserViewState(isLoading = false))
                errorLiveData.postValue(it)
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