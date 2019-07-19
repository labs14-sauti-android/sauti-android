package com.labs.sauti.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
* Border Procedues
* Req Documents
* Border Agencies
* Tax Calculator
* Regulated goods
* */


@Entity(tableName = "trade_info")
data class TradeInfoData (

    @SerializedName("tradeinfoid")
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tradeinfo_id")
    val tradeinfoID: Long = 0
)