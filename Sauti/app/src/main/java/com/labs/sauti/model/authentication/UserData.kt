package com.labs.sauti.model.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("userid")
    @Expose
    var id: Long? = null,

    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null
)