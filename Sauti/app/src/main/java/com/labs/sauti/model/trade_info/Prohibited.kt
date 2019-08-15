package com.labs.sauti.model.trade_info

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "prohibited_goods")
data class Prohibited(

    @SerializedName("name")
    @Expose
    val name: String
)