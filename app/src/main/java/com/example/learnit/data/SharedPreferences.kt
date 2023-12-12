package com.example.learnit.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.learnit.ui.App

object SharedPreferences {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences =
            App.instance.getSharedPreferences("MyPreferences", Activity.MODE_PRIVATE)
    }

    fun storeToken(token: String) {
        sharedPreferences.edit().putString(Constants.TOKEN, token).apply()
    }

    fun storeExpires(expires: Long) {
        sharedPreferences.edit().putLong(Constants.EXPIRATION_TIME, expires).apply()
    }

    fun storeUserId(userId: Int) {
        sharedPreferences.edit().putLong(Constants.ID, userId.toLong()).apply()
    }

    fun storeAdmin(admin: Boolean) {
        sharedPreferences.edit().putBoolean(Constants.ADMIN, admin).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(Constants.TOKEN, null)
    }

    fun getExpiresTime(): Long {
        return sharedPreferences.getLong(Constants.EXPIRATION_TIME, 0)
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(Constants.ID, 0)
    }

    fun getAdmin(): Boolean {
        return sharedPreferences.getBoolean(Constants.ADMIN, false)
    }

    fun getStudent(): Boolean {
        return sharedPreferences.getBoolean(Constants.STUDENT, false)
    }

    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(Constants.TOKEN)
            remove(Constants.EXPIRATION_TIME)
            remove(Constants.ID)
            remove(Constants.ADMIN)
        }.apply()
    }
    private const val PREFS_NAME = "LearnITPrefs"
    private const val DARK_MODE_STATUS = "darkModeStatus"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getDarkModeStatus(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(DARK_MODE_STATUS, false)
    }

    fun setDarkModeStatus(context: Context, isEnabled: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(DARK_MODE_STATUS, isEnabled)
        editor.apply()
    }

    fun saveFontSize(context: Context, selectedFontSize: Int) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("fontSize", selectedFontSize)
        editor.apply()
    }
}