package com.sauti.sauti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null

    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null

    @SerializedName("expires_in")
    @Expose
    var expiresIn: Long? = null

    @SerializedName("scope")
    @Expose
    var scope: String? = null
}