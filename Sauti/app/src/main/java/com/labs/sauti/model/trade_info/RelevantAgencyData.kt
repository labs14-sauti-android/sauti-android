package com.labs.sauti.model.trade_info

import androidx.room.Entity

@Entity(tableName = "relevant_agency")
data class RelevantAgencyData(
    val agencyDescription: String,
    val agencyName: String
)