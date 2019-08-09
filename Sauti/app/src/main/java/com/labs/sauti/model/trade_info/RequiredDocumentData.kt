package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "required_document")
data class RequiredDocumentData(
    val docDescription: String,
    @PrimaryKey
    val docTitle: String
)