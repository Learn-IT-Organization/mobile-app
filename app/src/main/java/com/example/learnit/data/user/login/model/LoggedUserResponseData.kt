package com.example.learnit.data.user.login.model

import kotlinx.serialization.SerialName

data class LoggedUserResponseData (
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: LoggedUserData?,
    @SerialName("err")
    val error: ErrorLogin?,
    @SerialName("message")
    val message: String,
    @SerialName("servertime")
    val serverTime: Long
)