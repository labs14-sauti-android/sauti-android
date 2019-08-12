package com.labs.sauti.model.trade_info

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "procedure")
data class ProcedureData(

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("step")
    @Expose
    val step: Int
)