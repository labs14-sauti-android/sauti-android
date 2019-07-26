package com.labs.sauti.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
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
    var tradeinfoID: Long = 0,
    var tradeinfoTopic : String? = null,
    var tradeinfoTopicExpanded : String? = null,
    @Ignore
    var tradeinfoList : List<String> = listOf()
)
//TODO Markup with room annotations. Ideal way I would like data to be delivered in a list.
//TODO: Ignore will be removed for conversion.