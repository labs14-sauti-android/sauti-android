package com.labs.sauti.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tax_calculations")
data class TaxCalculationData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null


)