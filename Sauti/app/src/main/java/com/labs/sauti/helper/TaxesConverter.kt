package com.labs.sauti.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labs.sauti.model.trade_info.Taxes

class TaxesConverter {

    @TypeConverter
    fun toTaxes(taxes: String?): List<Taxes> {
        val type = object : TypeToken<List<Taxes>>(){}.type
        return Gson().fromJson(taxes, type)
    }

    @TypeConverter
    fun toTaxesJson(taxes: List<Taxes>?): String {
        val type = object : TypeToken<List<Taxes>>(){}.type
        return Gson().toJson(taxes, type)
    }
}