package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taxes")
data class Taxes(

    val taxPerc: String,
    @PrimaryKey
    val taxTitle: String
)