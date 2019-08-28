package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Restricted

class RestrictedConverter {

    @TypeConverter
    fun toRestricted(restricted: String?) :MutableList<Restricted>? =
        if(restricted == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<Restricted>>(){}.type
            Gson().fromJson(restricted, type)
        }

    @TypeConverter
    fun toRestrictedJson(restricted: MutableList<Restricted>?) :String? =
        if(restricted == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<Restricted>>(){}.type
            Gson().toJson(restricted, type)
        }
}