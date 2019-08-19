package com.labs.sauti.model.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("password")
    @Expose
    var password: String
)