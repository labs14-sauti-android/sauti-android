package com.labs.sauti.model

import com.google.gson.annotations.Expose

data class ReportForm(
    @Expose
    var border: String,
    @Expose
    var hasGoodExperience: Boolean
)