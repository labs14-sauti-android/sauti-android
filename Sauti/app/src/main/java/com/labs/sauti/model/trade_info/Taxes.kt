package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "taxes")
data class Taxes(

    @SerializedName("taxPerc")
    @Expose
    var taxPerc: String,

    @SerializedName("taxTitle")
    @Expose
    @PrimaryKey
    var taxTitle: String
)