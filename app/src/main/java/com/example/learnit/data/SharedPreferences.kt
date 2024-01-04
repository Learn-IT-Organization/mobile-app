package com.example.learnit.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.learnit.ui.App

object SharedPreferences {
    private val sharedPreferences: SharedPreferences

    init {
        //Ne legyen hardcoded
        sharedPreferences =
            App.instance.getSharedPreferences("MyPreferences", Activity.MODE_PRIVATE)
    }

    fun storeToken(token: String) {
        sharedPreferences.edit().putString(ApiConstants.TOKEN, token).apply()
    }

    fun storeExpires(expires: Long) {
        sharedPreferences.edit().putLong(ApiConstants.EXPIRATION_TIME, expires).apply()
    }

    fun storeUserId(userId: Long?) {
        sharedPreferences.edit().putLong(ApiConstants.USER_ID, userId!!).apply()
    }

    fun storeAdmin(admin: Boolean) {
        sharedPreferences.edit().putBoolean(ApiConstants.ADMIN, admin).apply()
    }
    fun storeTeacher(teacher: Boolean) {
        sharedPreferences.edit().putBoolean(ApiConstants.TEACHER, teacher).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(ApiConstants.TOKEN, null)
    }

    fun getExpiresTime(): Long {
        return sharedPreferences.getLong(ApiConstants.EXPIRATION_TIME, 0)
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(ApiConstants.USER_ID, 0)
    }

    fun getAdmin(): Boolean {
        return sharedPreferences.getBoolean(ApiConstants.ADMIN, false)
    }

    fun getTeacher(): Boolean {
        return sharedPreferences.getBoolean(ApiConstants.TEACHER, false)
    }
    fun getStudent(): Boolean {
        return sharedPreferences.getBoolean(ApiConstants.STUDENT, false)
    }

    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(ApiConstants.TOKEN)
            remove(ApiConstants.EXPIRATION_TIME)
            remove(ApiConstants.ID)
            remove(ApiConstants.ADMIN)
        }.apply()
    }
    // a valtozoknak az osztaly elejen van a helye!
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
        //vegyuk ki a hardecodolasokat
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("fontSize", selectedFontSize)
        editor.apply()
    }
}