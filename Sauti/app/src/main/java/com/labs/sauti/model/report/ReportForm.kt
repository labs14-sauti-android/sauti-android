package com.labs.sauti.model.report

import com.google.gson.annotations.Expose

data class ReportForm(
    @Expose
    var border: String,
    @Expose
    var hasGoodExperience: Boolean
)