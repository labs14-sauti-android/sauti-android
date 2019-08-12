package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(foreignKeys = [ForeignKey(entity = TradeInfoData::class,
    parentColumns = ["id"], childColumns = ["agencyName"])], tableName = "border_agency")
data class BorderAgencyData(

    @SerializedName("agencyDescription")
    @Expose
    val agencyDescription: String,

    @PrimaryKey
    @SerializedName("agencyName")
    @Expose
    val agencyName: String
)