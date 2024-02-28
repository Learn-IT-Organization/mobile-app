package com.example.learnit.domain.login

import com.example.learnit.data.courses.notifications.TokenData
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.ForgotPasswordData
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.data.user.login.model.LoginData
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.data.user.login.model.ResetCodeData
import com.example.learnit.data.user.login.model.ResetPasswordResponseData
import com.google.android.gms.common.api.Response

interface LoginRepository {
    suspend fun getLoginInformation(loginForm: LoginData): ResponseData<Data>
    suspend fun sendFCMToken(token: TokenData)
    suspend fun requestResetCode(forgotPasswordData: ForgotPasswordData): ResetPasswordResponseData
    suspend fun changePasswordWithResetCode(resetCodeData: ResetCodeData): ResetPasswordResponseData
}