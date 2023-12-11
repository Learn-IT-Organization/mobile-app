package com.example.learnit.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.user.login.repository.LoginRepositoryImpl
import com.example.learnit.data.user.login.repository.UserRepositoryImpl
import com.example.learnit.data.user.register.repository.RegisterRepositoryImpl
import com.example.learnit.domain.login.repository.LoginRepository
import com.example.learnit.domain.register.repository.RegisterRepository
import com.example.learnit.domain.user.repository.UserRepository

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val isDarkModeEnabled = SharedPreferences.getDarkModeStatus(this)
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun getLoginRepository(): LoginRepository = LoginRepositoryImpl
    fun getUserRepository(): UserRepository = UserRepositoryImpl
    fun getRegisterRepository(): RegisterRepository = RegisterRepositoryImpl
}