package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.BorderAgency

class BorderAgencyConverter {

    @TypeConverter
    fun toBorderAgency(borderAgency: String?) :List<BorderAgency> {
        val type = object : TypeToken<List<BorderAgency>>(){}.type
        return Gson().fromJson(borderAgency, type)
    }

    @TypeConverter
    fun toBorderAgencyJson(borderAgency: List<BorderAgency>?) :String {
        val type = object : TypeToken<List<BorderAgency>>(){}.type
        return Gson().toJson(borderAgency, type)
    }

}