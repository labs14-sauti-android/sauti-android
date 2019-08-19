package com.labs.sauti.model.trade_info

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "sensitive_goods")
data class Sensitive(

    @SerializedName("name")
    @Expose
    val name: String
)