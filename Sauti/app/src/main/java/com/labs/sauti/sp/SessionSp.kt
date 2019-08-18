package com.labs.sauti.sp

import android.content.Context
import com.google.gson.GsonBuilder
import com.labs.sauti.model.authentication.UserData

// TODO rxjava
class SessionSp(private val context: Context) {

    companion object {
        private const val SP_NAME = "session"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_EXPIRES_IN = "expires_in"
        private const val KEY_LOGGED_IN_AT = "logged_in_at"
        private const val KEY_USER = "user"

        private const val ELAPSED_TIME_BIAS = 60L
    }

    private val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun setAccessToken(accessToken: String) {
        val editor = sp.edit()
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun getAccessToken(): String {
        return sp.getString(KEY_ACCESS_TOKEN, "")!!
    }

    fun setExpiresIn(expiresIn: Long) {
        val editor = sp.edit()
        editor.putLong(KEY_EXPIRES_IN, expiresIn)
        editor.apply()
    }

    fun getExpiresIn(): Long {
        return sp.getLong(KEY_EXPIRES_IN, 0)
    }

    /** Seconds */
    fun setLoggedInAt(loggedInAt: Long) {
        val editor = sp.edit()
        editor.putLong(KEY_LOGGED_IN_AT, loggedInAt)
        editor.apply()
    }

    fun getLoggedInAt(): Long {
        return sp.getLong(KEY_LOGGED_IN_AT, 0)
    }

    fun isAccessTokenValid(): Boolean {
        // no session recorded
        if (getAccessToken() == "") {
            return false
        }

        val currentTimeSec = System.currentTimeMillis() / 1000L
        val timeElapsed = currentTimeSec - getLoggedInAt() + ELAPSED_TIME_BIAS

        // session expired
        if (timeElapsed > getExpiresIn()) {
            return false
        }

        return true
    }

    fun invalidateToken() {
        setAccessToken("")
    }

    fun getUser(): UserData? {
        val userStr = sp.getString(KEY_USER, null) ?: return null
        return GsonBuilder().create().fromJson(userStr, UserData::class.java)
    }

    fun setUser(user: UserData?) {
        val editor = sp.edit()
        if (user == null) {
            editor.remove(KEY_USER)
        } else {
            editor.putString(KEY_USER, GsonBuilder().create().toJson(user))
        }
        editor.apply()
    }

}