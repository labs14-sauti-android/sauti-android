package com.labs.sauti.model.trade_info

import androidx.room.Entity

@Entity(tableName = "taxes")
data class TaxesData(
    val taxPerc: String,
    val taxTitle: String
)