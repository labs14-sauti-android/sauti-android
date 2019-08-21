package com.labs.sauti.model.error

import com.google.gson.annotations.Expose

class ErrorDetailData(
    @Expose
    var title: String,

    @Expose
    var statusCode: Int,

    @Expose
    var errorCode: Int,

    @Expose
    var detail: String,

    @Expose
    var timestamp: String,

    @Expose
    var developerMessage: String
)