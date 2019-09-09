package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.RequiredDocument

class RequiredDocumentConverter {

    @TypeConverter
    fun toRequiredDocument(reqDoc: String?): MutableList<RequiredDocument>? =
        if(reqDoc == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<RequiredDocument>>(){}.type
            Gson().fromJson(reqDoc, type)
        }

    @TypeConverter
    fun toRequiredDocumentJson(reqDoc: MutableList<RequiredDocument>?): String? =
        if(reqDoc == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<RequiredDocument>>(){}.type
            Gson().toJson(reqDoc, type)
        }
}

