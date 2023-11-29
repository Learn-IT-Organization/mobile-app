package com.example.learnit.ui

import android.app.Application
import com.example.learnit.data.user.login.repository.UserRepositoryImpl
import com.example.learnit.domain.user.UserRepository

class App : Application() {
    companion object {
        lateinit var instance: App
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getUserRepository(): UserRepository = UserRepositoryImpl
}