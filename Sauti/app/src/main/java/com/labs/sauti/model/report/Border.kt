package com.labs.sauti.model.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Border(
    @SerializedName("border_name")
    @Expose
    var borderName: String? = null,

    @SerializedName("border_countries")
    @Expose
    var borderCountries: MutableList<String> = mutableListOf()
)