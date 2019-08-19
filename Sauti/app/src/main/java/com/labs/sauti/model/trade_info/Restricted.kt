package com.labs.sauti.model.trade_info

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restricted_goods")
data class Restricted(

    @SerializedName("name")
    @Expose
    val name: String
)