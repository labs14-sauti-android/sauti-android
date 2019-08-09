package com.labs.sauti.model.trade_info

import androidx.room.Entity

@Entity(tableName = "required_document")
data class RequiredDocumentData(
    val docDescription: String,
    val docTitle: String
)