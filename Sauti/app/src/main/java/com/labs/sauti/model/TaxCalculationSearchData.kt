package com.labs.sauti.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tax_calculation_searches")
data class TaxCalculationSearchData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var category: String,
    var product: String,
    var whereTo: String,
    var whereFrom: String,
    var value: Long
)