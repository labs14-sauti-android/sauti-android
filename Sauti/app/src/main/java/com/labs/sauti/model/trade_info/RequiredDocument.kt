package com.labs.sauti.model.trade_info

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "required_document")
data class RequiredDocument(

    @SerializedName("docDescription")
    @Expose
    var docDescription: String,

    @SerializedName("docTitle")
    @Expose
    var docTitle: String
)