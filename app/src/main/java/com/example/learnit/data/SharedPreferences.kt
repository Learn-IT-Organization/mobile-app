package com.example.learnit.data

import android.app.Activity
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

    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(Constants.TOKEN)
            remove(Constants.EXPIRATION_TIME)
            remove(Constants.ID)
            remove(Constants.ADMIN)
        }.apply()
    }

}