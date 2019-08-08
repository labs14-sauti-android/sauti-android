package com.labs.sauti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StringArray(
    @SerializedName("data")
    @Expose
    var data: MutableList<String>? = null
)