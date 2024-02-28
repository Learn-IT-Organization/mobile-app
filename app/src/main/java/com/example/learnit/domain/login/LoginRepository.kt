package com.example.learnit.domain.login

import com.example.learnit.data.courses.notifications.TokenData
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.LoginData
import com.example.learnit.data.user.login.model.ResponseData

interface LoginRepository {
    suspend fun getLoginInformation(loginForm: LoginData): ResponseData<Data>
    suspend fun sendFCMToken(token: TokenData)
}