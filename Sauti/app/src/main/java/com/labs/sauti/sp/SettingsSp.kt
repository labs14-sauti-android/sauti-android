package com.labs.sauti.sp

import android.content.Context

class SettingsSp(private val context: Context) {

    companion object {
        private const val SP_NAME = "settings"
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
    }

    private val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun getSelectedLanguage(): String {
        return sp.getString(KEY_SELECTED_LANGUAGE, "English")!!
    }

    fun setSelectedLanguage(language: String) {
        sp.edit()
            .putString(KEY_SELECTED_LANGUAGE, language)
            .apply()
    }

}