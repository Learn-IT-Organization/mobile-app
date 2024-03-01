package com.learnitevekri.domain.login

import com.learnitevekri.data.courses.notifications.TokenData
import com.learnitevekri.data.user.login.model.Data
import com.learnitevekri.data.user.login.model.ForgotPasswordData
import com.learnitevekri.data.user.login.model.LoginData
import com.learnitevekri.data.user.login.model.ResetCodeData
import com.learnitevekri.data.user.login.model.ResetPasswordResponseData
import com.learnitevekri.data.user.login.model.ResponseData

interface LoginRepository {
    suspend fun getLoginInformation(loginForm: LoginData): ResponseData<Data>
    suspend fun sendFCMToken(token: TokenData)
    suspend fun requestResetCode(forgotPasswordData: ForgotPasswordData): ResetPasswordResponseData
    suspend fun changePasswordWithResetCode(resetCodeData: ResetCodeData): ResetPasswordResponseData
}