package com.learnitevekri.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.learnitevekri.ui.App

object SharedPreferences {
    private val sharedPreferences: SharedPreferences =
        App.instance.getSharedPreferences("MyPreferences", Activity.MODE_PRIVATE)

    private const val PREFS_NAME = "LearnITPrefs"
    private const val MY_PREFS = "MyPrefs"
    private const val DARK_MODE_STATUS = "darkModeStatus"
    private const val IMAGE_PATH = "userImagePath"
    private const val REMEMBERED_USERNAME = "remembered_username"
    private const val REMEMBERED_PASSWORD = "remembered_password"
    private const val LAST_VIEWED_POSITION = "last_viewed_position"


    fun storeToken(token: String) {
        sharedPreferences.edit().putString(com.learnitevekri.data.ApiConstants.TOKEN, token).apply()
    }

    fun storeExpires(expires: Long) {
        sharedPreferences.edit().putLong(com.learnitevekri.data.ApiConstants.EXPIRATION_TIME, expires).apply()
    }

    fun storeUserId(userId: Long?) {
        sharedPreferences.edit().putLong(com.learnitevekri.data.ApiConstants.USER_ID, userId!!).apply()
    }

    fun storeAdmin(admin: Boolean) {
        sharedPreferences.edit().putBoolean(com.learnitevekri.data.ApiConstants.ADMIN, admin).apply()
    }

    fun storeTeacher(teacher: Boolean) {
        sharedPreferences.edit().putBoolean(com.learnitevekri.data.ApiConstants.TEACHER, teacher).apply()
    }

    fun storeStudent(student: Boolean) {
        sharedPreferences.edit().putBoolean(com.learnitevekri.data.ApiConstants.STUDENT, student).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(com.learnitevekri.data.ApiConstants.TOKEN, null)
    }

    fun getExpiresTime(): Long {
        return sharedPreferences.getLong(com.learnitevekri.data.ApiConstants.EXPIRATION_TIME, 0)
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(com.learnitevekri.data.ApiConstants.USER_ID, 0)
    }

    fun getAdmin(): Boolean {
        return sharedPreferences.getBoolean(com.learnitevekri.data.ApiConstants.ADMIN, false)
    }

    fun getTeacher(): Boolean {
        return sharedPreferences.getBoolean(com.learnitevekri.data.ApiConstants.TEACHER, false)
    }

    fun getStudent(): Boolean {
        return sharedPreferences.getBoolean(com.learnitevekri.data.ApiConstants.STUDENT, false)
    }

    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(com.learnitevekri.data.ApiConstants.TOKEN)
            remove(com.learnitevekri.data.ApiConstants.EXPIRATION_TIME)
            remove(com.learnitevekri.data.ApiConstants.ID)
            remove(com.learnitevekri.data.ApiConstants.ADMIN)
        }.apply()
    }

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
        val sharedPreferences = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("fontSize", selectedFontSize)
        editor.apply()
    }

    fun getUserImagePath(context: Context): String? {
        return getSharedPreferences(context).getString(IMAGE_PATH, null)
    }

    fun setUserImagePath(context: Context, imagePath: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(IMAGE_PATH, imagePath)
        editor.apply()
    }

    fun saveRememberedCredentials(context: Context, username: String, password: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(REMEMBERED_USERNAME, username)
        editor.putString(REMEMBERED_PASSWORD, password)
        editor.apply()
    }

    fun getRememberedUsername(context: Context): String? {
        return getSharedPreferences(context).getString(REMEMBERED_USERNAME, null)
    }

    fun getRememberedPassword(context: Context): String? {
        return getSharedPreferences(context).getString(REMEMBERED_PASSWORD, null)
    }

    fun clearRememberedCredentials(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(REMEMBERED_USERNAME)
        editor.remove(REMEMBERED_PASSWORD)
        editor.apply()
    }
    fun saveLastViewedPosition(position: Int) {
        sharedPreferences.edit().putInt(LAST_VIEWED_POSITION, position).apply()
    }

    fun getLastViewedPosition(): Int {
        return sharedPreferences.getInt(LAST_VIEWED_POSITION, 0)
    }
}