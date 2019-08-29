package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Sensitive

class SensitiveConverter {

    @TypeConverter
    fun toSensitive(sensitive: String?) :MutableList<Sensitive>? =
        if(sensitive == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<Sensitive>>(){}.type
            Gson().fromJson(sensitive, type)
        }

    @TypeConverter
    fun toSensitiveJson(sensitive: MutableList<Sensitive>?) :String? =
        if(sensitive == null ) {
            null
        } else {
            val type = object : TypeToken<MutableList<Sensitive>>(){}.type
            Gson().toJson(sensitive, type)
        }
}