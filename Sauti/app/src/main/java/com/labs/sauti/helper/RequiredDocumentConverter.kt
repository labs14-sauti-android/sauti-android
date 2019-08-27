package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.RequiredDocument

class RequiredDocumentConverter {

    @TypeConverter
    fun toRequiredDocument(reqDoc: String?): List<RequiredDocument> {
        val type = object : TypeToken<List<RequiredDocument>>(){}.type
        return Gson().fromJson(reqDoc, type)
    }

    @TypeConverter
    fun toRequiredDocumentJson(reqDoc: List<RequiredDocument>?): String {
        val type = object : TypeToken<List<RequiredDocument>>(){}.type
        return Gson().toJson(reqDoc, type)
    }
}

