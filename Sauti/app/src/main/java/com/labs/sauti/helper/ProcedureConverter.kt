package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Procedure

class ProcedureConverter {

    @TypeConverter
    fun toProcedure(procedure: String?): MutableList<Procedure>? =
        if(procedure == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<Procedure>>(){}.type
            Gson().fromJson(procedure, type)
        }

    @TypeConverter
    fun toProcedureJson(procedure: MutableList<Procedure>?): String? =
        if(procedure == null) {
            null
        } else {
            val type = object : TypeToken<MutableList<Procedure>>(){}.type
            Gson().toJson(procedure, type)
        }

}