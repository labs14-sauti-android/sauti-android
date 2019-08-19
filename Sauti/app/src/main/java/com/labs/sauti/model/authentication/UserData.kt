package com.labs.sauti.model.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("userId")
    @Expose
    var userId: Long? = null,

    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null,

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("location")
    @Expose
    var location: String? = null,

    @SerializedName("gender")
    @Expose
    var gender: String? = null
)