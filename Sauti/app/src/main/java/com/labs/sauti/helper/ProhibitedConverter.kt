package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Prohibited

class ProhibitedConverter {

    @TypeConverter
    fun toProhibited(prohibited: String) :List<Prohibited> {
        val type = object : TypeToken<List<Prohibited>>(){}.type
        return Gson().fromJson(prohibited, type)
    }

    @TypeConverter
    fun toProhibitedJson(prohibited: List<Prohibited>?) :String {
        val type = object : TypeToken<List<Prohibited>>(){}.type
        return Gson().toJson(prohibited, type)
    }
}