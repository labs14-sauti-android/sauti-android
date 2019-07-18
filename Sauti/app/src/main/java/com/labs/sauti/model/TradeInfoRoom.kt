package com.labs.sauti.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Border Procedues
* Req Documents
* Border Agencies
* Tax Calculator
* Regulated goods
* */


@Entity(tableName = "trade_info")
data class TradeInfoRoom (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tradeinfo_id")
    val tradeinfoID: Long = 0
)