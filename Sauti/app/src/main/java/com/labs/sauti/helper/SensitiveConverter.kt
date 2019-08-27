package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Sensitive

class SensitiveConverter {

    @TypeConverter
    fun toSensitive(Sensitive: String?) :List<Sensitive> {
        val type = object : TypeToken<List<Sensitive>>(){}.type
        return Gson().fromJson(Sensitive, type)
    }

    @TypeConverter
    fun toSensitiveJson(Sensitive: List<Sensitive>?) :String {
        val type = object : TypeToken<List<Sensitive>>(){}.type
        return Gson().toJson(Sensitive, type)
    }
}