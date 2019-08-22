package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.labs.sauti.model.authentication.SignUpRequest
import com.labs.sauti.model.authentication.User
import com.labs.sauti.model.error.ErrorDetailData
import com.labs.sauti.repository.UserRepository
import com.labs.sauti.view_state.authentication.*
import retrofit2.HttpException

const val ERROR_CODE_INVALID_USERNAME = 1
const val ERROR_CODE_INVALID_PASSWORD = 2
const val ERROR_CODE_USERNAME_TAKEN = 3

class AuthenticationViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<String>() }
    private val signUpViewState by lazy { MutableLiveData<SignUpViewState>() }
    private val signInViewState by lazy { MutableLiveData<SignInViewState>() }
    private val signOutViewState by lazy { MutableLiveData<SignOutViewState>() }
    private val signedInUserViewState by lazy { MutableLiveData<SignedInUserViewState>() }

    fun getErrorLiveData(): LiveData<String> = errorLiveData
    fun getSignUpViewState(): LiveData<SignUpViewState> = signUpViewState
    fun getSignInViewState(): LiveData<SignInViewState> = signInViewState
    fun getSignOutViewState(): LiveData<SignOutViewState> = signOutViewState

    /** No signed in user if userId is null*/
    fun getSignedInUserViewState(): LiveData<SignedInUserViewState> = signedInUserViewState

    fun signUp(signUpRequest: SignUpRequest) {
        signUpViewState.value = SignUpViewState(isLoading = true)
        addDisposable(userRepository.signUp(signUpRequest).subscribe(
            {
                signUpViewState.postValue(SignUpViewState(isLoading = false, isSuccess = true))
            },
            {
                signUpViewState.postValue(SignUpViewState(isLoading = false, isSuccess = false))
                if (it is HttpException) {
                    try {
                        val errorDetail = GsonBuilder().create()
                            .fromJson(it.response()?.errorBody()?.string(), ErrorDetailData::class.java)

                        val errorMessage = when (errorDetail.errorCode) {
                            ERROR_CODE_INVALID_USERNAME -> "Invalid username"
                            ERROR_CODE_INVALID_PASSWORD -> "Invalid password"
                            ERROR_CODE_USERNAME_TAKEN -> "Username taken"
                            else -> "An unknown error has occurred"
                        }

                        errorLiveData.postValue(errorMessage)

                    } catch (e: JsonSyntaxException) {
                        errorLiveData.postValue("An unknown error has occurred")
                    }
                }
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
                errorLiveData.postValue("Failed to sign in")
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
                errorLiveData.postValue("Failed to sign out")
            }
        ))
    }

    fun getSignedInUser() {
        signedInUserViewState.value = SignedInUserViewState(isLoading = true)
        addDisposable(userRepository.getSignedInUser()
            .map {
                User(
                    it.userId,
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
                errorLiveData.postValue("An unknown error has occurred")
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