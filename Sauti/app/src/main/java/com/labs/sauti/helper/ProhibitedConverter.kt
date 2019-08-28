package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Prohibited

class ProhibitedConverter {

    @TypeConverter
    fun toProhibited(prohibited: String?) :MutableList<Prohibited>? =
        if (prohibited == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<Prohibited>>(){}.type
            Gson().fromJson(prohibited, type)
        }

    @TypeConverter
    fun toProhibitedJson(prohibited: MutableList<Prohibited>?) :String? =
        if (prohibited != null) {
            val type = object : TypeToken<MutableList<Prohibited>>(){}.type
            Gson().toJson(prohibited, type)
        } else {
            null
        }
}