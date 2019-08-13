package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Restricted

class RestrictedConverter {

    @TypeConverter
    fun toRestricted(restricted: String) :List<Restricted> {
        val type = object : TypeToken<List<Restricted>>(){}.type
        return Gson().fromJson(restricted, type)
    }

    @TypeConverter
    fun toRestrictedJson(restricted: List<Restricted>?) :String {
        val type = object : TypeToken<List<Restricted>>(){}.type
        return Gson().toJson(restricted, type)
    }
}