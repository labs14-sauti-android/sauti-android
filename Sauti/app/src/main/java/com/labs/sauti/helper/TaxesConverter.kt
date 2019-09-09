package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Taxes

class TaxesConverter {

    @TypeConverter
    fun toTaxes(taxes: String?): MutableList<Taxes>? =
        if(taxes == null) {
            null
        } else {
            val type = object : TypeToken<List<Taxes>>() {}.type
            Gson().fromJson(taxes, type)
        }

    @TypeConverter
    fun toTaxesJson(taxes: MutableList<Taxes>?): String? =
        if(taxes == null) {
            null
        } else {
            val type = object : TypeToken<List<Taxes>>(){}.type
            Gson().toJson(taxes, type)
        }
}