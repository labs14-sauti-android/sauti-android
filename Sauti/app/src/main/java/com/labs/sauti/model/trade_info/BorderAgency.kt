package com.labs.sauti.model.trade_info

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "border_agency")
data class BorderAgency(

    @SerializedName("agencyDescription")
    @Expose
    val agencyDescription: String,

    @SerializedName("agencyName")
    @Expose
    val agencyName: String

)