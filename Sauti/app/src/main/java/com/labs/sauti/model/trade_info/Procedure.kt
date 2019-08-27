package com.labs.sauti.model.trade_info

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Procedure(

    @SerializedName("description")
    @Expose
    val description: String?= null,

    @SerializedName("step")
    @Expose
    val step: Int?= null
)