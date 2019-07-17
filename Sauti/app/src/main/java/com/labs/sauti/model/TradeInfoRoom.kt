package com.labs.sauti.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/*
* Border Procedues
* Req Documents
* Border Agencies
* Tax Calculator
* Regulated goods
* */


data class TradeInfoRoom (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tradeinfo_id")
    val productID: Long = 0
)