package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Procedure

class ProcedureConverter {

    @TypeConverter
    fun toProcedure(procedure: String): List<Procedure> {
        val type = object : TypeToken<List<Procedure>>(){}.type
        return Gson().fromJson(procedure, type)
    }

    @TypeConverter
    fun toProcedureJson(procedure: List<Procedure>?): String {
        val type = object : TypeToken<List<Procedure>>(){}.type
        return Gson().toJson(procedure, type)
    }

}