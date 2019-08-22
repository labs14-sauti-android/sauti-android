package com.labs.sauti.model.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null,

    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null,

    @SerializedName("expires_in")
    @Expose
    var expiresIn: Long? = null,

    @SerializedName("scope")
    @Expose
    var scope: String? = null
)