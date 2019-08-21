package com.labs.sauti.converter

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class StringMutableListConverter {

    @TypeConverter
    fun toStringMutableList(jsonStr: String): MutableList<String>? {
        val type = object : TypeToken<MutableList<String>>() {}.type
        return GsonBuilder().create().fromJson<MutableList<String>>(jsonStr, type)
    }

    @TypeConverter
    fun toString(stringMutableList: MutableList<String>): String {
        return GsonBuilder().create().toJson(stringMutableList)
    }

}