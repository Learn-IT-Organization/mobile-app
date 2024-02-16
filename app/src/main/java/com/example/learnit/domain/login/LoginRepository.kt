package com.example.learnit.domain.login

import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.data.user.login.model.LoginData

interface LoginRepository {
    suspend fun getLoginInformation(loginForm: LoginData): ResponseData<Data>

}