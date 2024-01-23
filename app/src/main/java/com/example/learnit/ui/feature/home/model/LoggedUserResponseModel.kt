package com.example.learnit.ui.feature.home.model

import com.example.learnit.data.user.login.model.LoggedUserData

data class LoggedUserResponseModel(
    val success: Boolean,
    val data: LoggedUserData,
    val err: Any,
    val message: String,
    val servertime: Long
)