package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.BorderAgency

class BorderAgencyConverter {

    @TypeConverter
    fun toBorderAgency(data: String?) :MutableList<BorderAgency>? =
        if(data == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<BorderAgency>>(){}.type
            Gson().fromJson(data, type)
        }

    @TypeConverter
    fun toBorderAgencyJson(borderAgency: List<BorderAgency>?) :String? =
        if (borderAgency == null) {
            null
        } else {
            val type = object : TypeToken<List<BorderAgency>>() {}.type
            Gson().toJson(borderAgency, type)
        }

}