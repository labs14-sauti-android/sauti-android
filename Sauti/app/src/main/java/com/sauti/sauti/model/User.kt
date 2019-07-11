package com.sauti.sauti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("userid")
    @Expose
    var id: Long? = null

    @SerializedName("username")
    @Expose
    var username: String? = null
}