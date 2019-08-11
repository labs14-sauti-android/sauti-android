package com.labs.sauti.model.trade_info

import androidx.room.Entity

@Entity(tableName = "border_agency")
data class BorderAgencyData(
    val agencyDescription: String,
    val agencyName: String
)