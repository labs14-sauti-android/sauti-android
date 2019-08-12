package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Restricted

class RestrictedConverter {

    @TypeConverter
    fun toRestricted(Restricted: String) :List<Restricted> {
        val type = object : TypeToken<List<Restricted>>(){}.type
        return Gson().fromJson(Restricted, type)
    }

    @TypeConverter
    fun toRestrictedJson(Restricted: List<Restricted>?) :String {
        val type = object : TypeToken<List<Restricted>>(){}.type
        return Gson().toJson(Restricted, type)
    }
}