package com.labs.sauti.model

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException

class SautiApiError {

    companion object {
        fun fromThrowable(throwable: Throwable): SautiApiError {
            return if (throwable is HttpException) {
                val errorBody = throwable.response().errorBody()
                if (errorBody == null) {
                    SautiApiError()
                } else {
                    GsonBuilder().create()
                        .fromJson(errorBody.charStream().readText(), SautiApiError::class.java)
                }
            } else {
                SautiApiError()
            }
        }
    }

    @SerializedName("error")
    @Expose
    var error: String? = "Unknown Error"

    @SerializedName("error_description")
    @Expose
    var errorDescription: String? = "An unknown error has occurred"
}